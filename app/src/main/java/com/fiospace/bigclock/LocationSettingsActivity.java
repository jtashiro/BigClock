package com.fiospace.bigclock;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationSettingsActivity extends AppCompatActivity {
    private static final String TAG = "LocationSettingsActivity";

    public static final String PREF_LOCATION_MODE = "LOCATION_MODE";
    public static final String LOCATION_MODE_DEVICE = "DEVICE";
    public static final String LOCATION_MODE_ZIP = "ZIP";
    public static final String PREF_ZIP_CODE = "ZIP_CODE";
    public static final String PREF_COUNTRY_CODE = "COUNTRY_CODE";
    public static final String PREF_ZIP_LATITUDE = "ZIP_LATITUDE";
    public static final String PREF_ZIP_LONGITUDE = "ZIP_LONGITUDE";

    private SharedPreferences sharedPreferences;
    private RadioGroup locationModeRadioGroup;
    private RadioButton radioDeviceLocation;
    private RadioButton radioZipCountry;
    private EditText zipCodeEditText;
    private EditText countryCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        locationModeRadioGroup = findViewById(R.id.locationModeRadioGroup);
        radioDeviceLocation = findViewById(R.id.radioDeviceLocation);
        radioZipCountry = findViewById(R.id.radioZipCountry);
        zipCodeEditText = findViewById(R.id.zipCodeEditText);
        countryCodeEditText = findViewById(R.id.countryCodeEditText);
        Button saveLocationButton = findViewById(R.id.saveLocationButton);

        String mode = sharedPreferences.getString(PREF_LOCATION_MODE, LOCATION_MODE_DEVICE);
        if (LOCATION_MODE_ZIP.equals(mode)) {
            radioZipCountry.setChecked(true);
        } else {
            radioDeviceLocation.setChecked(true);
        }

        zipCodeEditText.setText(sharedPreferences.getString(PREF_ZIP_CODE, ""));
        countryCodeEditText.setText(sharedPreferences.getString(PREF_COUNTRY_CODE, ""));

        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void saveSettings() {
        boolean useZip = locationModeRadioGroup.getCheckedRadioButtonId() == R.id.radioZipCountry;

        if (!useZip) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_LOCATION_MODE, LOCATION_MODE_DEVICE);
            editor.apply();
            finish();
            return;
        }

        String zipCode = zipCodeEditText.getText().toString().trim();
        String countryCode = countryCodeEditText.getText().toString().trim().toUpperCase(Locale.US);

        if (zipCode.isEmpty() || countryCode.isEmpty()) {
            Toast.makeText(this, "Enter both a zip/postal code and country code", Toast.LENGTH_SHORT).show();
            return;
        }

        double[] latLon = geocodeZipCountry(zipCode, countryCode);
        if (latLon == null) {
            Toast.makeText(this, "Could not resolve that zip/postal code", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LOCATION_MODE, LOCATION_MODE_ZIP);
        editor.putString(PREF_ZIP_CODE, zipCode);
        editor.putString(PREF_COUNTRY_CODE, countryCode);
        editor.putFloat(PREF_ZIP_LATITUDE, (float) latLon[0]);
        editor.putFloat(PREF_ZIP_LONGITUDE, (float) latLon[1]);
        editor.apply();

        finish();
    }

    private double[] geocodeZipCountry(String zipCode, String countryCode) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String query = zipCode + ", " + countryCode;
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new double[]{address.getLatitude(), address.getLongitude()};
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoder failed for " + query, e);
        }
        return null;
    }
}
