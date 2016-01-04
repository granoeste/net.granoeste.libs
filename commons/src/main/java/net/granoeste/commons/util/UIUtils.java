/*
 * Copyright (C) 2014 granoeste.net http://granoeste.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.granoeste.commons.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class UIUtils {

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMr2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasMashmarrow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    private static final int PHONE_MAX_SIZE = 6; // 6 inch

    public static boolean isTabletDevice(Context context) {
        final DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        // Work width and height ( unit = pixel )
        final int widthPx = metrics.widthPixels;
        final int heightPx = metrics.heightPixels;

        // Work dpi (xdpi, ydpi)
        final float xdpi = metrics.xdpi;
        final float ydpi = metrics.ydpi;

        //Work  width and height( unit= inch)
        final float widthIn = widthPx / xdpi;
        final float heightIn = heightPx / ydpi;

        // Calculate size
        final double in = Math.sqrt(widthIn * widthIn + heightIn * heightIn);
        if (PHONE_MAX_SIZE < in) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("InlinedApi")
    public static void hideSystemUI(Activity activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
//        if (UIUtils.hasHoneycombMR1()) {
//            activity.getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
//        }

        if (!UIUtils.hasHoneycombMR1()) {
            return;
        }
        int newUiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();;
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (UIUtils.hasICS()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        // Status bar hiding: Backwards compatible to Jellybean
        if (UIUtils.hasJellyBean()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (UIUtils.hasKitkat()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @SuppressLint("InlinedApi")
    public static void hideSystemUIImmersiveSticky(Activity activity) {
        // Set the IMMERSIVE STICKY flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
//        if (UIUtils.hasHoneycombMR1()) {
//            activity.getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }

        if (!UIUtils.hasHoneycombMR1()) {
            return;
        }
        int newUiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();;
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (UIUtils.hasICS()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        // Status bar hiding: Backwards compatible to Jellybean
        if (UIUtils.hasJellyBean()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (UIUtils.hasKitkat()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    @SuppressLint("InlinedApi")
    public static void showSystemUI(Activity activity) {
//        if (UIUtils.hasHoneycombMR1()) {
//            activity.getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        if (!UIUtils.hasHoneycombMR1()) {
            return;
        }
        int newUiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (UIUtils.hasICS()) {
            newUiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        // Status bar hiding: Backwards compatible to Jellybean
        if (UIUtils.hasJellyBean()) {
            newUiOptions &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (UIUtils.hasKitkat()) {
            newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
            newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

}
