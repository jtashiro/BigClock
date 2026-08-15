package com.fiospace.bigclock.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.preference.PreferenceManager;

public class BackgroundImageManager {
    private static final String TAG = BackgroundImageManager.class.getSimpleName();

    private static final int INTERVAL_HOUR = 3600000; // 1 hour in milliseconds
    private int interval = 60000;  // every minute
    private Context context;
    private View layoutView;
    private View toolbar;
    private int currentImageIndex = 0;
    private int currentIndex = 0;
    private int[] imageResources;
    private int[] colors = {Color.RED, Color.BLUE}; // Add your desired colors here

    public BackgroundImageManager(Context context, View layoutView, View toolbar, int[] imageResources, int interval) {
        this.context = context;
        this.layoutView = layoutView;
        this.toolbar = toolbar;
        this.imageResources = imageResources;
        this.interval = interval * 1000;

        // Start changing the background image periodically
        startBackgroundImageChange();
    }

    private void startBackgroundImageChange() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeBackgroundImage();
                handler.postDelayed(this, interval); // Repeat after INTERVAL_HOUR milliseconds
            }
        }, interval); // Start after INTERVAL_HOUR milliseconds
    }

    private void changeBackgroundImage() {
        String uiPrefKey = "background_color";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String backgroundColor = sharedPreferences.getString(uiPrefKey, "#FFFFFF");
        SharedPreferences.Editor editor = sharedPreferences.edit();

        currentImageIndex = (currentImageIndex + 1) % imageResources.length;

        // Get the integer color value
        int color = colors[currentImageIndex] ;
        // Convert the integer color value to a hexadecimal string
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        // Store the hexadecimal color string in SharedPreferences
        editor.putString(uiPrefKey, hexColor);
        editor.apply();

        // Toggle between the two images
        layoutView.setBackgroundColor(imageResources[currentImageIndex]);
        toolbar.setBackgroundColor(imageResources[currentImageIndex]);

        Log.i(TAG,"changing background image from " + backgroundColor + " to " + hexColor);
        SharedPreferencesUtils.printSharedPreferences(context);

    }
}
