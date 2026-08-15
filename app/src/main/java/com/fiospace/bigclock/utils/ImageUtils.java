package com.fiospace.bigclock.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;

public class ImageUtils {

    public static void setImageFromBytes(byte[] imageData, ImageView imageView) {
        new ImageLoadTask(imageData, imageView).execute();
    }

    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private final byte[] imageData;
        private final ImageView imageView;

        public ImageLoadTask(byte[] imageData, ImageView imageView) {
            this.imageData = imageData;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            return BitmapFactory.decodeStream(inputStream);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // Set a placeholder image or handle the error
            }
        }
    }
}
