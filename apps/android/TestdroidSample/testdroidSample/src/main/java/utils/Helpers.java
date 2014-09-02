package utils;

import android.content.Context;
import android.os.Build;
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

    private static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private static boolean hasSoftNavigation(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
        return false;
    }

}
