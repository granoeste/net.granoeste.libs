package net.granoeste.commons.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import static net.granoeste.commons.util.LogUtils.makeLogTag;

public class DisplayUtils {
    @SuppressWarnings("unused")
    private static String TAG = makeLogTag(DisplayUtils.class);

    /**
     * ディスプレイサイズ取得
     *
     * @param context
     * @return ディスプレイ横サイズ
     */
    public static int getDisplayWidth(Context context) {
        return getDisplaySize(context).x;
    }

    /**
     * ディスプレイサイズ取得
     *
     * @param context
     * @return ディスプレイ縦サイズ
     */
    public static int getDisplayHeight(Context context) {
        return getDisplaySize(context).y;
    }

    /**
     * ディスプレイサイズ取得
     *
     * @param context
     * @return Point ディスプレイサイズ
     */
    public static Point getDisplaySize(Context context) {
        Point size = null;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            size = new Point();
            display.getSize(size);
        } else {
            // under API level 12
            size = new Point();
            size.x = display.getWidth(); // deprecated
            size.y = display.getHeight(); // deprecated
        }
        return size;
    }

    /**
     * ナビゲーションバーサイズ取得
     *
     * @param context
     * @return
     */
    public static int getNavigationBarSize(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return res.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * ナビゲーションバーの高さを取得
     *
     * @param context
     * @return 下側表示時の高さ、右側表示時はゼロ
     */
    public static int getNavigationBarHeight(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return getNavigationBarSize(context);

        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Tabletはナビゲーションバーが右側では無く下側に表示される
            if (UIUtils.isTabletDevice(context)) {
                return getNavigationBarSize(context);
            }
        }
        // ナビゲーションバーが右側に表示されているとき
        return 0;
    }

    /**
     * ナビゲーションバーの幅を取得
     *
     * @param context
     * @return 右側表示時の幅、下側表示時はゼロ
     */
    public static int getNavigationBarWidth(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Tabletはナビゲーションバーが右側では無く下側に表示される
            if (UIUtils.isTabletDevice(context)) {
                return 0;
            }
            return getNavigationBarSize(context);
        }
        // ナビゲーションバーが下側に表示されているとき
        return 0;
    }

    /**
     * Is landscape
     *
     * @param context Context
     * @return true is landscape
     */
    public static boolean isLandscape(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    /**
     * Is large screen
     *
     * @param context Context
     * @return true is large screen
     */
    public static boolean isLargeScreen(Context context) {
        return getScreenLayoutSize(context) == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Is xlarge screen
     *
     * @param context Context
     * @return true is xlarge screen
     */
    public static boolean isXlargeScreen(Context context) {
        return getScreenLayoutSize(context) == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * get screen layout size
     *
     * @param context Context
     * @return screen layout size
     *  <ul>
     *  <li>Configuration.SCREENLAYOUT_SIZE_XLARGE(720x960 dp units)</li>
     *  <li>Configuration.SCREENLAYOUT_SIZE_LARGE(480x640 dp units)</li>
     *  <li>Configuration.SCREENLAYOUT_SIZE_NORMAL(320x470 dp units)</li>
     *  <li>Configuration.SCREENLAYOUT_SIZE_SMALL(320x426 dp units)</li>
     *  </ul>
     */
    public static int getScreenLayoutSize(Context context) {
        // get screen layout size
        return context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    }

}
