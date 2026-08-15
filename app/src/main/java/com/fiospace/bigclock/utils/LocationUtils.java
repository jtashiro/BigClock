package com.fiospace.bigclock.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    public static String getPostalCodeFromLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String postalCode = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                sb.append(address.getPostalCode()).append(":");
                sb.append(address.getCountryCode());
                postalCode = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postalCode;
    }

    public static String getAddressFromLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String addressText = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                /*
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                 */
                sb.append(address.getSubAdminArea()).append(", ");
                sb.append(address.getSubLocality()).append(", ");
                sb.append(address.getAdminArea()).append(", ");
                sb.append(address.getPostalCode()).append(" ");
                sb.append(address.getCountryCode());


                addressText = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressText;
    }
}

