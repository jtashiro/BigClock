package com.fiospace.bigclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fiospace.bitcoin_price_fetcher.BitcoinPriceFetcher;
import com.fiospace.bigclock.datasources.homebrew.ForecastData;
import com.fiospace.bigclock.datasources.homebrew.WeatherData;
import com.fiospace.bigclock.datasources.nws.NationalWeatherServiceAPIClient;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener  {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private Toolbar toolbar;
    private MaterialTextView textViewTime;
    private MaterialTextView textViewDate;
    private MaterialTextView textViewWeather;

    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean showColon = true;

    private Handler weatherUpdateHandler;
    private Runnable weatherUpdateRunnable;
    private Handler marketUpdateHandler;
    private Runnable marketUpdateRunnable;
    private int marketUpdateFrequency = 60000 * 5; // 5 minutes
    private int updateFrequency = 60000 * 60; // Default frequency in milliseconds (1 hour)

    private SharedPreferences sharedPreferences;

    private MaterialTextView textViewBTC;
    private ExecutorService executorService;

    String marketDataSource = "Coinbase";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Register SharedPreferences change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        textViewTime = findViewById(R.id.textViewTime);
        textViewDate = findViewById(R.id.textViewDate);
        textViewWeather = findViewById(R.id.textViewWeather);
        textViewBTC = findViewById(R.id.textViewBTC);
        //adjustFontSizes();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        executorService = Executors.newSingleThreadExecutor();

        runnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        startWeatherUpdates();
        startMarketUpdates();
        //fetchMarketData();
    }

    private void getLocationAndFetchWeather() {
        String locationMode = sharedPreferences.getString(
                LocationSettingsActivity.PREF_LOCATION_MODE, LocationSettingsActivity.LOCATION_MODE_DEVICE);

        if (LocationSettingsActivity.LOCATION_MODE_ZIP.equals(locationMode)) {
            double lat = sharedPreferences.getFloat(LocationSettingsActivity.PREF_ZIP_LATITUDE, 0f);
            double lon = sharedPreferences.getFloat(LocationSettingsActivity.PREF_ZIP_LONGITUDE, 0f);
            fetchWeather(lat, lon);
            return;
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    fetchWeather(lat, lon);
                } else {
                    Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchWeather(double lat, double lon) {
        Log.i(TAG, "fetchWeather API call");

        WeatherData weatherData = new WeatherData();
        ForecastData forecastData = new ForecastData();

        NationalWeatherServiceAPIClient client = new NationalWeatherServiceAPIClient(
                lat, lon, weatherData, forecastData,
                result -> {
                    if (result != null) {
                        int roundedTemp = Math.round(Float.parseFloat(weatherData.getTemperature()));
                        String tempStringH = getString(R.string.temp, roundedTemp);
                        runOnUiThread(() -> textViewWeather.setText(tempStringH));
                    } else {
                        Log.e(TAG, "Failed to fetch weather from National Weather Service");
                    }
                }, this);
        client.execute();
    }

    private void startWeatherUpdates() {
        weatherUpdateHandler = new Handler(Looper.getMainLooper());
        weatherUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                getLocationAndFetchWeather();
                weatherUpdateHandler.postDelayed(this, updateFrequency);
            }
        };
        weatherUpdateHandler.post(weatherUpdateRunnable);
    }

    private void stopWeatherUpdates() {
        if (weatherUpdateHandler != null && weatherUpdateRunnable != null) {
            weatherUpdateHandler.removeCallbacks(weatherUpdateRunnable);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocationAndFetchWeather();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTime() {
        String currentDate = new SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(new Date());
        textViewDate.setText(currentDate);

        // Format the time to show only hours and minutes
        String currentTime = new SimpleDateFormat(showColon ? "h:mm" : "h mm", Locale.getDefault()).format(new Date());
        textViewTime.setText(currentTime);

        // Toggle the colon every second
        showColon = !showColon;
        showColon = true;
    }

    /**
     * async calls to get market prices
     */
    private void startMarketUpdates() {
        marketUpdateHandler = new Handler(Looper.getMainLooper());
        marketUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                fetchMarketData();
                marketUpdateHandler.postDelayed(this, marketUpdateFrequency);
            }
        };
        marketUpdateHandler.post(marketUpdateRunnable);
    }

    private void stopMarketUpdates() {
        if (marketUpdateHandler != null && marketUpdateRunnable != null) {
            marketUpdateHandler.removeCallbacks(marketUpdateRunnable);
        }
    }

    private void fetchMarketData() {
        executorService.execute(() -> {
            try {
                /**/
                //String marketDataSource = "Coinbase";
                String formattedPrice = BitcoinPriceFetcher.getPrice(marketDataSource);
                Log.i(TAG,marketDataSource + " BTC Price: " + formattedPrice);
                runOnUiThread(() -> textViewBTC.setText(formattedPrice));

                /**
                Log.i(TAG, "fetchMarketData() API call.");

                URL url = new URL("https://api.coinbase.com/v2/prices/spot?currency=USD");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    String result = response.toString();
                    JSONObject jsonObject = new JSONObject(result);
                    double btcPrice = jsonObject.getJSONObject("data").getDouble("amount");
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                    currencyFormat.setMaximumFractionDigits(0);
                    String formattedPrice = currencyFormat.format(btcPrice);
                    runOnUiThread(() -> textViewBTC.setText(formattedPrice));
                    Log.i(TAG, "BTC Price: " + formattedPrice);
                } finally {
                    urlConnection.disconnect();
                }
                 **/
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopWeatherUpdates();
        stopMarketUpdates();
        executorService.shutdown();
        handler.removeCallbacks(runnable);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

    }

    private float getFontScale() {
        Configuration configuration = getResources().getConfiguration();
        return configuration.fontScale;
    }

    private void adjustFontSizes() {
        float fontScale = getFontScale();
        Log.i(TAG, "fontScale=" + fontScale);

        // Adjust the font sizes based on the scale
        textViewTime.setTextSize(500 * fontScale);
        textViewDate.setTextSize(80 * fontScale);
        textViewWeather.setTextSize(125 * fontScale);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MainActivity", "onCreateOptionsMenu called");

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected called with item id: " + item.getItemId());

        if (item.getItemId() == R.id.action_location_settings) {
            Intent intent = new Intent(this, LocationSettingsActivity.class);
            startActivity(intent);
            return true;
        }

        // choose font for time
        if (item.getItemId() == R.id.action_choose_font) {
            // Show font chooser dialog
            showFontChooserDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFontChooserDialog() {
        // Create and show the font chooser dialog
        // You can use DialogFragment or create a custom dialog
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"onResume():" );

        super.onResume();
        getLocationAndFetchWeather();
    }

    @Override
    protected void onPause() {
        Log.i(TAG,"onPause():");

        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG,"key:" + key);
    }
}
