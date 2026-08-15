package com.fiospace.bigclock.datasources.nws;

import android.content.Context;
import android.os.AsyncTask;

import com.fiospace.bigclock.datasources.homebrew.ForecastData;
import com.fiospace.bigclock.datasources.homebrew.WeatherData;
import com.fiospace.bigclock.utils.LocationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NationalWeatherServiceAPIClient extends AsyncTask<Void, Void, String> {
    private double latitude;
    private double longitude;
    private WeatherDataListener listener;

    private WeatherData weatherData;
    private ForecastData forecastData;
    private Context context;

    public NationalWeatherServiceAPIClient(double latitude, double longitude,
                                           WeatherData weatherData, ForecastData forecastData,
                                           WeatherDataListener listener, Context context) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.listener = listener;
        this.context = context;

        this.weatherData = weatherData;
        this.forecastData = forecastData;
    }

    private String call_nws_api() {
        try {
            int gridX = 0, gridY = 0;
            String office = new String();
            String apiUrl = "https://api.weather.gov/points/" + latitude + "," + longitude;
            System.out.println("** National weather service /points URL "+ apiUrl);

            URL url = new URL(apiUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // Set request headers
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // get Location information here
                // set the forecastData.location params
                String address = LocationUtils.getAddressFromLocation(context, latitude, longitude);

                this.weatherData.setObservationDateTime();

                weatherData.setLocation(this.weatherData.getObservationDateTime() + " " + address);

                // parse the response and extract the forecast URL
                NationalWeatherServicePointsData nwsPointsData = new NationalWeatherServicePointsData(response.toString());

                // Accessing properties of the NWSPointsData object
                String id = nwsPointsData.getForecastOfficeUrl();

                return id;
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * depends on the call_nws_weather_api() call to return the apiUrl that is input parameter.
     *
     */
    private String call_nws_forecast_api(String apiUrl ) {
        try {
            /*
            int gridX = 0, gridY = 0;
            String office = new String();
            String forecastUrl = "https://api.weather.gov/" + office + "/" + gridX + "," + gridY + "/forecast";
            */

            System.out.println("** National weather forecast service URL "+ apiUrl);
            URL url = new URL(apiUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set request headers
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // populate the weatherData and forecastData objects that were passed in
                populateDataObjects(response.toString());

                return response.toString();
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        String forecast_url = call_nws_api();
        if (forecast_url != null) {
            String forecastData = call_nws_forecast_api(forecast_url);
            return forecastData;
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String weatherData) {
        if (listener != null) {
            listener.onWeatherDataReceived(weatherData);
        }
    }

    public interface WeatherDataListener {
        void onWeatherDataReceived(String weatherData);
    }

    /**
     * Populates the weatherData and forecastData objects that were passed into the class via
     * constructor.  This is a mapping function taking the data and putting into format suitable
     * for use in the UI.
     *
     * @param weatherDataString
     */
    private void populateDataObjects(String weatherDataString) {
        // parse the response and extract the forecast URL
        NationalWeatherServiceForecastData nwsForecastData = NationalWeatherServiceForecastData.fromJson(weatherDataString);

        // Accessing properties of the NWSPointsData object
        List<NationalWeatherServiceForecastData.Properties.Period> periods = nwsForecastData.getProperties().getPeriods();

        NationalWeatherServiceForecastData.Properties.Period today = periods.get(0);
        this.weatherData.setCurrentCondition(today.getShortForecast());
        this.weatherData.setTemperature(today.getTemperature().toString());
        this.weatherData.setFeelsLike(today.getTemperature().toString());
        this.weatherData.setWindSpeed(today.getWindSpeed());
        this.weatherData.setIconUrl(today.getIcon());
        this.weatherData.setObservationDateTime();

        this.forecastData.clearForecastEntries();
        for (int i=0; i<5; i++) {
            ForecastData.ForecastEntry fe = new ForecastData.ForecastEntry();
            fe.setDescription(periods.get(i).getName());
            fe.setHigh(periods.get(i).getTemperature().toString());
            fe.setLow(periods.get(i).getTemperature().toString());
            fe.setImageUrl(periods.get(i).getIcon());
            fe.setShortPrediction(periods.get(i).getShortForecast());

            this.forecastData.addForecastEntry(fe);
        }
    }
}

