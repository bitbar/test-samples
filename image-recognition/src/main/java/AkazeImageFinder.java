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
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Testdroid Akaze Image Finder
 *
 * https://git@github.com/bitbar/testdroid-samples
 *
 * Usage:
 *
 * @TODO
 *
 * @author support@bitbar.com
 */

public class AkazeImageFinder {
    private static final Logger logger = LoggerFactory.getLogger(AkazeImageFinder.class);
    public String rotation;
    public double scene_height;
    public double scene_width;

    public AkazeImageFinder() {
        rotation = "notSet";
    }

    public AkazeImageFinder(String setRotation) {
        rotation = setRotation;
    }

    public Point[] findImage(String object_filename_nopng, String scene_filename_nopng) {
        //default tolerance level is 0.6 - this is used when calculating differences in size and ratio between the object and the found object
        //the lower the value, the stricter the matching
        return findImage(object_filename_nopng, scene_filename_nopng, 0.6);
    }

    public double getSceneHeight() {
        return scene_height;
    }

    public double getSceneWidth() {
        return scene_width;
    }

    public Point[] findImage(String object_filename_nopng, String scene_filename_nopng, double tolerance) {

        logger.info("AkazeImageFinder - findImage() started...");
        setupOpenCVEnv();
        String object_filename = object_filename_nopng + ".png";
        String scene_filename = scene_filename_nopng + ".png";

        Mat img_object = Highgui.imread(object_filename, Highgui.CV_LOAD_IMAGE_UNCHANGED);
        Mat img_scene = Highgui.imread(scene_filename, Highgui.CV_LOAD_IMAGE_UNCHANGED);
        scene_height = img_scene.rows();
        scene_width = img_scene.cols();
        logger.info("Scene height and width: " + scene_height + ", " + scene_width);

        //rotateImage(scene_filename, img_scene);
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


        logger.info("Keypoints for {} to be found in {} are in file {}", object_filename, scene_filename, jsonResults);

        double initial_height = img_object.size().height;
        double initial_width = img_object.size().width;

        logger.info("Initial size: " + initial_height + ", " + initial_width);

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
            logger.error("Not enough mathches found. ");
            return null;
        }

        MatOfPoint2f obj = new MatOfPoint2f();
        obj.fromList(objList);
        MatOfPoint2f scene = new MatOfPoint2f();
        scene.fromList(sceneList);

        Mat H = Calib3d.findHomography(obj, scene);

        //Mat scene_corners = drawFoundHomography(scene_filename_nopng, img_object, filename, H);
        Mat scene_corners = drawFoundHomography(scene_filename_nopng, img_object, scene_filename, H);
        Point top_left = new Point(scene_corners.get(0, 0));
        Point top_right = new Point(scene_corners.get(1, 0));
        Point bottom_left = new Point(scene_corners.get(3, 0));
        Point bottom_right = new Point(scene_corners.get(2, 0));

        //1rad = 360º/2π = 57.3º.

        double rotationAngle = round(getComponents(H) * 57.3 / 90, 0);

        logger.info("90 Degree Rotation: " + rotationAngle);

        Point[] objectOnScene = new Point[5];

        //TO-DO add other values:
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


        //calculateRotation = calculateImageRotation(calculateRotation, top_left, top_right, bottom_left, bottom_right);
        //Point center = new Point(top_left.x + (top_right.x - top_left.x) / 2, top_left.y + (bottom_left.y - top_left.y) / 2);
        Point center = new Point(objectOnScene[0].x + (objectOnScene[1].x - objectOnScene[0].x) / 2, objectOnScene[0].y + (objectOnScene[3].y - objectOnScene[0].y) / 2);
        logger.info("Image found at coordinates: " + (int) center.x + ", " + (int) center.y + " on scene.");

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


        if (checkFoundImageDimensions(top_left, top_right, bottom_left, bottom_right, tolerance))
            return null;
        if (checkFoundImageSizeRatio(initial_height, initial_width, top_left, top_right, bottom_left, bottom_right, initial_ratio, found_ratio1, found_ratio2, tolerance))
            return null;

        //calculate points in original orientation
        Point[] points = new Point[5];

        if (rotationAngle == 1.0) {
            points[0] = new Point(scene_height - bottom_left.y, bottom_left.x);
            points[1] = new Point(scene_height - top_left.y, top_left.x);
            points[2] = new Point(scene_height - top_right.y, top_right.x);
            points[3] = new Point(scene_height - bottom_right.y, bottom_right.x);
        } else if (rotationAngle == -1.0) {
            points[0] = new Point(top_right.y, scene_width - top_right.x);
            points[1] = new Point(bottom_right.y, scene_width - bottom_right.x);
            points[2] = new Point(bottom_left.y, scene_width - bottom_left.x);
            points[3] = new Point(top_left.y, scene_width - top_left.x);
        } else if (rotationAngle == 2.0) {
            points[0] = new Point(scene_width - bottom_right.x, scene_height - bottom_right.y);
            points[1] = new Point(scene_width - bottom_left.x, scene_height - bottom_left.y);
            points[2] = new Point(scene_width - top_left.x, scene_height - top_left.y);
            points[3] = new Point(scene_width - top_right.x, scene_height - top_right.y);
        } else {
            points[0] = top_left;
            points[1] = top_right;
            points[2] = bottom_right;
            points[3] = bottom_left;
        }

        Point centerOriginal = new Point(points[0].x + (points[1].x - points[0].x) / 2, points[0].y + (points[3].y - points[0].y) / 2);

        points[4] = centerOriginal;

        logger.info("Top left original: " + points[0].x + ", " + points[0].y);
        logger.info("Top right original: " + points[1].x + ", " + points[1].y);
        logger.info("Bottom right original: " + points[2].x + ", " + points[2].y);
        logger.info("Bottom left original: " + points[3].x + ", " + points[3].y);
        logger.info("Center: " + points[4].x + ", " + points[4].y);

        return points;

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


        //filename = scene_filename_nopng + "_with_results.png";

        filename = scene_filename_nopng + ".png";
        Highgui.imwrite(filename, img);

        return scene_corners;
    }

    private boolean checkFoundImageSizeRatio(double initial_height, double initial_width, Point top_left, Point top_right, Point bottom_left, Point bottom_right, double initial_ratio, double found_ratio1, double found_ratio2, double tolerance) {
        //check the image size, if too small incorrect image was found - only if rotation has been set, otherwise points will be incorrect

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
        //check any big differences in hight and width on each side
        double left_height = bottom_left.y - top_left.y;
        double right_height = bottom_right.y - top_right.y;
        double height_ratio = round(left_height / right_height, 2);


        double top_width = top_right.x - top_left.x;
        double bottom_width = bottom_right.x - bottom_left.x;
        double width_ratio = round(top_width / bottom_width, 2);

        if ((height_ratio == 0) || (width_ratio == 0)) {
            return false;
        }


        logger.info("Height and width ratios: " + height_ratio + " and " + width_ratio);

        if ((height_ratio < (1 - tolerance)) || (height_ratio > (1 + tolerance)) || (width_ratio < (1 - tolerance)) || (width_ratio > (1 + tolerance))) {
            logger.info("Height and width ratios: " + height_ratio + " and " + width_ratio);
            logger.error("Image found is not the correct shape, height or width are different on each side.");
            return true;
        } else {
            return false;
        }    }

     private String runAkazeMatch(String object_filename, String scene_filename) throws InterruptedException, IOException {

        long timestamp = System.currentTimeMillis();
        String jsonFilename = "./target/keypoints/keypoints_" + timestamp + ".json";
        logger.info("Json file should be found at: {}", jsonFilename);
        File file = new File(jsonFilename);
        file.getParentFile().mkdirs();
        String akazePath = "";
        if (System.getProperty("os.name").toString().toLowerCase().contains("mac")) {
          akazePath = "akaze/mac/akaze_match";
        } else if (System.getProperty("os.name").toString().toLowerCase().contains("win")) {
          akazePath = "akaze/win/akaze_match";
        } else {
          akazePath = "akaze/linux/akaze_match";
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
                System.out.print(".");
            int exitVal = proc.waitFor();
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

    private void setupOpenCVEnv() {
        //System.setProperty("java.library.path", "/usr/local/lib/");
        logger.info(System.getProperty("os.name"));
        String platformName = System.getProperty("os.name");
        if (platformName.contains("Mac OS X")) {
            System.setProperty("java.library.path", "/opt/opencv249/share/OpenCV/java/");
        }
        else {
            System.setProperty("java.library.path", "/usr/local/share/OpenCV/java/");
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
        double c = h.get(0, 2)[0];
        double d = h.get(1, 0)[0];
        double e = h.get(1, 1)[0];
        double f = h.get(1, 2)[0];

        double p = Math.sqrt(a * a + b * b);
        double r = (a * e - b * d) / (p);
        double q = (a * d + b * e) / (a * e - b * d);
        double theta = Math.atan2(b, a);


        logger.info("Translation: " + c + ", " + f);
        logger.info("Scale: " + p + ", " + r);
        logger.info("Shear: " + q);
        logger.info("Theta: " + theta);


        return theta;
//
    }


    public void rotateImage90n(Mat source, Mat dest, int angle) {
        // angle : factor of 90, even it is not factor of 90, the angle will be mapped to the range of [-360, 360].
        // {angle = 90n; n = {-4, -3, -2, -1, 0, 1, 2, 3, 4} }
        // if angle bigger than 360 or smaller than -360, the angle will be mapped to -360 ~ 360
        // mapping rule is : angle = ((angle / 90) % 4) * 90;
        //
        // ex : 89 will map to 0, 98 to 90, 179 to 90, 270 to 3, 360 to 0.

        source.copyTo(dest);

        angle = ((angle / 90) % 4) * 90;

        int flipHorizontalOrVertical;
        //0 : flip vertical; 1 flip horizontal
        if (angle > 0) {
            flipHorizontalOrVertical = 0;
        } else {
            flipHorizontalOrVertical = 1;
        }

        int number = (int) (angle / 90);

        for (int i = 0; i != number; ++i) {
            Core.transpose(dest, dest);
            Core.flip(dest, dest, flipHorizontalOrVertical);
        }
    }

    public String getRotation() {
        return rotation;
    }

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException, JSONException {
        String object;

        try {
            object = args[0];
        } catch (Exception e) {
            object = "object";
        }
        System.out.println(System.getProperty("user.dir"));


        int min_multiplier_tolerance = 8;
        System.out.println();
        System.out.println("Checking image: " + object);

        String scene = "./target/reports/" + object + "_screenshot";

        String object_filename = "./queryimages/" + object;
        String scene_filename = scene;
        System.out.println(object_filename);
        System.out.println(scene_filename);
        AkazeImageFinder finder = new AkazeImageFinder();
        finder.rotation = "0 degrees";
        finder.findImage(object_filename, scene_filename);
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

}


class PointSortX implements Comparator<Point> {

    public int compare(Point a, Point b) {
        return (a.x < b.x) ? -1 : (a.x > b.x) ? 1 : 0;
    }
}


class PointSortY implements Comparator<Point> {

    public int compare(Point a, Point b) {
        return (a.y < b.y) ? -1 : (a.y > b.y) ? 1 : 0;
    }
}
