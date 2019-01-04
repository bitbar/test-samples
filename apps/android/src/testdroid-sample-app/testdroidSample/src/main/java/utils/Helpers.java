package utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.testdroid.sample.android.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class Helpers {

    private static final String TAG = Helpers.class.getName().toString();

    public static Pair<Integer, Integer> getWindowDimensionsWithoutMargin(Context context, WindowManager windowManager, int dpMarginX, int dpMarginY) {

        dpMarginX *= 2;
        dpMarginY *= 2;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int marginX = dpToPx(context, dpMarginX);
        if (hasSoftNavigation(context)) {
            dpMarginY += 30;
        }
        int marginY = dpToPx(context, dpMarginY);

        Pair<Integer, Integer> dimensions = new Pair<Integer, Integer>(screenWidth - marginX, screenHeight - marginY);

        return dimensions;
    }

    public static int getDimenDp(Context context, int dimenId) {
        return ((int) (context.getResources().getDimension(dimenId) / context.getResources().getDisplayMetrics().density));
    }

    public static void toastDefault(Context context, String text, int duration) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_default, null);

        TextView textView = (TextView) view.findViewById(R.id.toast_default_text);
        textView.setText(text);

        Toast toast = new Toast(context);
        // toast.setGravity(Gravity.TOP, 0, 250);
        toast.setDuration(duration);
        toast.setView(view);
        toast.show();
    }

    public static boolean isGpsOn(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsEnabled) {
            return true;
        } else {
            return false;
        }
    }

    public static void toastWarning(Context context, String text, int duration) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_warning, null);

        TextView textView = (TextView) view.findViewById(R.id.toast_warning_text);
        textView.setText(text);

        Toast toast = new Toast(context);
        // toast.setGravity(Gravity.TOP, 0, 250);
        toast.setDuration(duration);
        toast.setView(view);
        toast.show();
    }

    private static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private static boolean hasSoftNavigation(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
        return false;
    }

    public static double round(double unrounded, int precision) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_DOWN);
        return rounded.doubleValue();
    }

    public static String epochToHuman(long epoch) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss.SSS");
        return simpleDateFormat.format(new Date(epoch));
    }

    public static int randomInt(int min, int max) {
        Random random = new Random();
        int randomInt = random.nextInt((max - min) + 1) + min;
        return randomInt;
    }

    public static String getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static void vibrate(Context context, long[] pattern) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, -1);
    }

}
