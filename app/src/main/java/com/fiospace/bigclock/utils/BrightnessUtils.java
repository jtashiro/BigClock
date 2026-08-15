package com.fiospace.bigclock.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

public class BrightnessUtils {

    // Method to check if the app has permission to modify system settings
    public static boolean canWriteSettings(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return Settings.System.canWrite(context);
        }
        return true;
    }

    // Method to change the brightness level
    public static void setBrightness(Context context, int brightness) {
        if (canWriteSettings(context)) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
            Window window = getWindow(context);
            if (window != null) {
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.screenBrightness = brightness / 255.0f;
                window.setAttributes(layoutParams);
            }
        }
    }

    // Method to get the current brightness level
    public static int getCurrentBrightness(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Method to get the Window object associated with the current window
    private static Window getWindow(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).getWindow();
        }
        return null;
    }
}
