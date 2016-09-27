import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import static org.opencv.imgproc.Imgproc.resize;

/**
 * Created by testdroid on 22/07/16.
 */
public class AkazeImageFinder {

    private static final Logger logger = LoggerFactory.getLogger(AkazeImageFinder.class);
    public double scene_height;
    public double scene_width;
    double lastResizeFactor;

    public double getSceneHeight() {
        return scene_height;
    }

    public double getSceneWidth() {
        return scene_width;
    }

    public Point[] findImage(String object_filename_nopng, String scene_filename_nopng, double tolerance) {

        long start_time = System.nanoTime();

        String object_filename = object_filename_nopng + ".png";
        String scene_filename = scene_filename_nopng + ".png";

        Mat img_object = Highgui.imread(object_filename, Highgui.CV_LOAD_IMAGE_UNCHANGED);
        Mat img_scene = Highgui.imread(scene_filename, Highgui.CV_LOAD_IMAGE_UNCHANGED);

        Mat resized_img_scene = new Mat();
        scene_height = img_scene.rows();
        scene_width = img_scene.cols();
        //logger.info("Scene height and width: " + scene_height + ", " + scene_width);

        double resizeFactor = 1;
        if (scene_width < scene_height)
            resizeFactor = scene_width / 750;
        else
            resizeFactor = scene_height / 750;

        if (resizeFactor > 1) {
            Size size = new Size(scene_width / resizeFactor, scene_height / resizeFactor);
            resize(img_scene, resized_img_scene, size);
            Highgui.imwrite(scene_filename, resized_img_scene);
            img_scene = Highgui.imread(scene_filename, Highgui.CV_LOAD_IMAGE_UNCHANGED);
            logger.info("Image was resized, resize factor is: " + resizeFactor);
            lastResizeFactor = resizeFactor;
        } else
            resizeFactor = 1;

        String jsonResults = null;
        try {
            jsonResults = runAkazeMatch(object_filename, scene_filename);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (jsonResults == null) {
            return null;
        }

        double initial_height = img_object.size().height;
        double initial_width = img_object.size().width;

        Highgui.imwrite(scene_filename, img_scene);

        //finding homography
        LinkedList<Point> objList = new LinkedList<Point>();
        LinkedList<Point> sceneList = new LinkedList<Point>();
        JSONObject jsonObject = getJsonObject(jsonResults);
        if (jsonObject == null) {
            logger.error("ERROR: Json file couldn't be processed. ");
            return null;
        }
        JSONArray keypointsPairs = null;
        try {
            keypointsPairs = jsonObject.getJSONArray("keypoint-pairs");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Point[] objPoints = new Point[keypointsPairs.length()];
        Point[] scenePoints = new Point[keypointsPairs.length()];
        int j = 0;
        for (int i = 0; i < keypointsPairs.length(); i++) {
            try {
                objPoints[j] = new Point(Integer.parseInt(keypointsPairs.getJSONObject(i).optString("x1")), Integer.parseInt(keypointsPairs.getJSONObject(i).optString("y1")));
                scenePoints[j] = new Point(Integer.parseInt(keypointsPairs.getJSONObject(i).optString("x2")), Integer.parseInt(keypointsPairs.getJSONObject(i).optString("y2")));
                j++;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        for (int i = 0; i < objPoints.length; i++) {
            Point objectPoint = new Point(objPoints[i].x, objPoints[i].y);
            objList.addLast(objectPoint);
            Point scenePoint = new Point(scenePoints[i].x - initial_width, scenePoints[i].y);
            sceneList.addLast(scenePoint);
        }

        if ((objList.size() < 4) || (sceneList.size() < 4)) {
            logger.error("Not enough matches found. ");
            return null;
        }

        MatOfPoint2f obj = new MatOfPoint2f();
        obj.fromList(objList);
        MatOfPoint2f scene = new MatOfPoint2f();
        scene.fromList(sceneList);

        Mat H = Calib3d.findHomography(obj, scene);

        Mat scene_corners = drawFoundHomography(scene_filename_nopng, img_object, scene_filename, H);
        Point top_left = new Point(scene_corners.get(0, 0));
        Point top_right = new Point(scene_corners.get(1, 0));
        Point bottom_left = new Point(scene_corners.get(3, 0));
        Point bottom_right = new Point(scene_corners.get(2, 0));


        double rotationAngle = round(getComponents(H) * 57.3 / 90, 0);

        Point[] objectOnScene = new Point[5];

        if (rotationAngle == 1.0) {
            objectOnScene[0] = top_right;
            objectOnScene[1] = bottom_right;
            objectOnScene[2] = bottom_left;
            objectOnScene[3] = top_left;
        } else if (rotationAngle == -1.0) {
            objectOnScene[0] = bottom_left;
            objectOnScene[1] = top_left;
            objectOnScene[2] = top_right;
            objectOnScene[3] = bottom_right;

        } else if (rotationAngle == 2.0) {
            objectOnScene[0] = bottom_right;
            objectOnScene[1] = bottom_left;
            objectOnScene[2] = top_left;
            objectOnScene[3] = top_right;
        } else {
            objectOnScene[0] = top_left;
            objectOnScene[1] = top_right;
            objectOnScene[2] = bottom_right;
            objectOnScene[3] = bottom_left;
        }

        Point center = new Point(objectOnScene[0].x + (objectOnScene[1].x - objectOnScene[0].x) / 2, objectOnScene[0].y + (objectOnScene[3].y - objectOnScene[0].y) / 2);

        top_left = objectOnScene[0];
        top_right = objectOnScene[1];
        bottom_right = objectOnScene[2];
        bottom_left = objectOnScene[3];

        double initial_ratio = 1.0;
        if ((rotationAngle == 1.0) || (rotationAngle == -1.0)) {
            initial_ratio = initial_width / initial_height;
        } else {
            initial_ratio = initial_height / initial_width;
        }
        double found_ratio1 = (bottom_left.y - top_left.y) / (top_right.x - top_left.x);
        double found_ratio2 = (bottom_right.y - top_right.y) / (bottom_right.x - bottom_left.x);

        long end_time = System.nanoTime();
        int difference = (int) ((end_time - start_time) / 1e6 / 1000);
        logger.info("==> Image finder took: " + difference + " secs.");

        if (checkFoundImageDimensions(top_left, top_right, bottom_left, bottom_right, tolerance))
            return null;
        if (checkFoundImageSizeRatio(initial_height, initial_width, top_left, top_right, bottom_left, bottom_right, initial_ratio, found_ratio1, found_ratio2, tolerance))
            return null;

        //calculate points in original orientation
        Point[] points = new Point[5];

        if (rotationAngle == 1.0) {
            points[0] = new Point(scene_height / resizeFactor - bottom_left.y, bottom_left.x);
            points[1] = new Point(scene_height / resizeFactor - top_left.y, top_left.x);
            points[2] = new Point(scene_height / resizeFactor - top_right.y, top_right.x);
            points[3] = new Point(scene_height / resizeFactor - bottom_right.y, bottom_right.x);
        } else if (rotationAngle == -1.0) {
            points[0] = new Point(top_right.y, scene_width / resizeFactor - top_right.x);
            points[1] = new Point(bottom_right.y, scene_width / resizeFactor - bottom_right.x);
            points[2] = new Point(bottom_left.y, scene_width / resizeFactor - bottom_left.x);
            points[3] = new Point(top_left.y, scene_width / resizeFactor - top_left.x);
        } else if (rotationAngle == 2.0) {
            points[0] = new Point(scene_width / resizeFactor - bottom_right.x, scene_height / resizeFactor - bottom_right.y);
            points[1] = new Point(scene_width / resizeFactor - bottom_left.x, scene_height / resizeFactor - bottom_left.y);
            points[2] = new Point(scene_width / resizeFactor - top_left.x, scene_height / resizeFactor - top_left.y);
            points[3] = new Point(scene_width / resizeFactor - top_right.x, scene_height / resizeFactor - top_right.y);
        } else {
            points[0] = top_left;
            points[1] = top_right;
            points[2] = bottom_right;
            points[3] = bottom_left;
        }

        Point centerOriginal = new Point((points[0].x + (points[1].x - points[0].x) / 2) * resizeFactor, (points[0].y + (points[3].y - points[0].y) / 2) * resizeFactor);

        points[4] = centerOriginal;

        logger.info("Image found at coordinates: " + (int) points[4].x + ", " + (int) points[4].y + " on screen.");
        logger.info("All corners: " + points[0].toString() + " " + points[1].toString() + " " + points[2].toString() + " " + points[4].toString());

        points[0] = new Point(points[0].x * resizeFactor, points[0].y * resizeFactor);
        points[1] = new Point(points[1].x * resizeFactor, points[1].y * resizeFactor);
        points[2] = new Point(points[2].x * resizeFactor, points[2].y * resizeFactor);
        points[3] = new Point(points[3].x * resizeFactor, points[3].y * resizeFactor);

        return points;
    }

    public void cropImage(String scene_filename_nopng, double x, double y, double width, double height) {

        String scene_filename = scene_filename_nopng + ".png";
        Mat img_object = Highgui.imread(scene_filename);

        Rect croppedRect = new Rect((int) (x / lastResizeFactor), (int) (y / lastResizeFactor), (int) (width / lastResizeFactor), (int) (height / lastResizeFactor));
        Mat croppedImage = new Mat(img_object, croppedRect);
        Highgui.imwrite(scene_filename, croppedImage);
    }

    private Mat drawFoundHomography(String scene_filename_nopng, Mat img_object, String filename, Mat h) {
        Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
        Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

        obj_corners.put(0, 0, new double[]{0, 0});
        obj_corners.put(1, 0, new double[]{img_object.cols(), 0});
        obj_corners.put(2, 0, new double[]{img_object.cols(), img_object.rows()});
        obj_corners.put(3, 0, new double[]{0, img_object.rows()});

        Core.perspectiveTransform(obj_corners, scene_corners, h);

        Mat img = Highgui.imread(filename, Highgui.CV_LOAD_IMAGE_COLOR);

        Core.line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
        Core.line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
        Core.line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
        Core.line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);

        filename = scene_filename_nopng + ".png";
        Highgui.imwrite(filename, img);

        return scene_corners;
    }

    private boolean checkFoundImageSizeRatio(double initial_height, double initial_width, Point top_left, Point top_right, Point bottom_left, Point bottom_right, double initial_ratio, double found_ratio1, double found_ratio2, double tolerance) {
        //check the image size, if too small incorrect image was found

        if ((round(found_ratio1 / initial_ratio, 2) > (1 + tolerance)) || (round(initial_ratio / found_ratio2, 2) > (1 + tolerance))
                || (round(found_ratio1 / initial_ratio, 2) < (1 - tolerance)) || (round(initial_ratio / found_ratio2, 2) < (1 - tolerance))) {
            logger.error("Size of image found is incorrect, check the ratios for more info:");
            logger.info("Initial height of query image: " + initial_height);
            logger.info("Initial width of query image: " + initial_width);
            logger.info("Initial ratio for query image: " + initial_height / initial_width);

            logger.info("Found top width: " + (top_right.x - top_left.x));
            logger.info("Found bottom width: " + (bottom_right.x - bottom_left.x));

            logger.info("Found left height: " + (bottom_left.y - top_left.y));
            logger.info("Found right height: " + (bottom_right.y - top_right.y));
            logger.info("Found ratio differences: " + round(found_ratio1 / initial_ratio, 1) + " and " + round(initial_ratio / found_ratio2, 1));
            return true;
        }
        return false;
    }

    private boolean checkFoundImageDimensions(Point top_left, Point top_right, Point bottom_left, Point bottom_right, double tolerance) {
        //check any big differences in height and width on each side
        double left_height = bottom_left.y - top_left.y;
        double right_height = bottom_right.y - top_right.y;
        double height_ratio = round(left_height / right_height, 2);


        double top_width = top_right.x - top_left.x;
        double bottom_width = bottom_right.x - bottom_left.x;
        double width_ratio = round(top_width / bottom_width, 2);

        if ((height_ratio == 0) || (width_ratio == 0)) {
            return false;
        }

        if ((height_ratio < (1 - tolerance)) || (height_ratio > (1 + tolerance)) || (width_ratio < (1 - tolerance)) || (width_ratio > (1 + tolerance))) {
            logger.info("Height and width ratios: " + height_ratio + " and " + width_ratio);
            logger.error("Image found is not the correct shape, height or width are different on each side.");
            return true;
        } else {
            return false;
        }
    }

    private String runAkazeMatch(String object_filename, String scene_filename) throws InterruptedException, IOException {

        long timestamp = System.currentTimeMillis();
        String jsonFilename = "./target/keypoints/keypoints_" + timestamp + ".json";
        //logger.info("Json file should be found at: {}", jsonFilename);
        File file = new File(jsonFilename);
        file.getParentFile().mkdirs();
        String platformName = System.getProperty("os.name");
        String akazePath = "";
        if (platformName.toLowerCase().contains("mac")) {
            akazePath = "lib/mac/akaze/akaze_match";
        } else if (platformName.toLowerCase().contains("win")) {
            akazePath = "lib/win/akaze/akaze_match";
        } else {
            akazePath = "lib/linux/akaze/akaze_match";
        }
        String[] akazeMatchCommand = {akazePath, object_filename, scene_filename, "--json", jsonFilename, "--dthreshold", "0.00000000001"};

        try {
            ProcessBuilder p = new ProcessBuilder(akazeMatchCommand);
            Process proc = p.start();
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
                System.out.print("");
            int exitVal = proc.waitFor();
            if (exitVal != 0)
                logger.info("Akaze matching process exited with value: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (!file.exists()) {
            logger.error("ERROR: Image recognition with Akaze failed. No json file created.");
            return null;
        } else {
            return jsonFilename;
        }
    }

    public static void setupOpenCVEnv() {
        String platformName = System.getProperty("os.name");
        logger.info(platformName);
        if (platformName.toLowerCase().contains("mac")) {
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + System.getProperty("user.dir") + "/lib/mac/opencv");
        } else if (platformName.toLowerCase().contains("win")) {
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + System.getProperty("user.dir") + "/lib/win/opencv");
        } else {
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + System.getProperty("user.dir") + "/lib/linux/opencv/java7");
        }

        Field fieldSysPath = null;
        try {
            fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        fieldSysPath.setAccessible(true);
        try {
            fieldSysPath.set(null, null);
        } catch (IllegalAccessException e) {
        }
        logger.info("java.library.path: " + System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private JSONObject getJsonObject(String filename) {
        File jsonFile = new File(filename);
        InputStream is = null;
        try {
            is = new FileInputStream(jsonFile);
            String jsonTxt = IOUtils.toString(is);
            return new JSONObject(jsonTxt);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getComponents(Mat h) {

        double a = h.get(0, 0)[0];
        double b = h.get(0, 1)[0];
        return Math.atan2(b, a);
    }

    public static double round(double value, int places) {
        try {
            if (places < 0) throw new IllegalArgumentException();
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static void log(String message) {
        logger.info(message);
    }
}
