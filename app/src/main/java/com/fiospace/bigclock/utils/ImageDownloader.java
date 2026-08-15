package com.fiospace.bigclock.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImageDownloader {

    private final OkHttpClient client;

    public ImageDownloader() {
        this.client = new OkHttpClient();
    }

    public byte[] downloadImage(String url, String userAgent) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", userAgent)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                return responseBody.bytes();
            } else {
                throw new IOException("Empty response body");
            }
        }
    }

    public static void main(String[] args) {
        ImageDownloader downloader = new ImageDownloader();
        String url = "https://api.weather.gov/icons/land/night/rain_showers?size=medium";
        String userAgent = "CustomUserAgent/1.0"; // Your custom user agent here

        try {
            byte[] imageData = downloader.downloadImage(url, userAgent);
            // Now you can use the image data as needed
            System.out.println("Image downloaded successfully. Size: " + imageData.length + " bytes");
        } catch (IOException e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }
}
