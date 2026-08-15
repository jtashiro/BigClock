package com.fiospace.bigclock.datasources.homebrew;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherFetcher extends AsyncTask<Void, Void, WeatherData> {
    private static final String TAG = WeatherFetcher.class.getSimpleName();
    private WeatherListener listener;
    String datasource_hostname = "a5775249777.api.wxbug.net";
    String apiUrl = "http://" + datasource_hostname + "/getLiveWeatherRSS.aspx?ACode=A5775249777&zipCode=10308&UnitType=0&OutputType=0";


    public WeatherFetcher(WeatherListener listener) {
        this.listener = listener;
    }

    @Override
    protected WeatherData doInBackground(Void... voids) {
        WeatherData weatherData = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            weatherData = parseWeatherData(inputStream);
            inputStream.close();
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error fetching weather data", e);
        }
        return weatherData;
    }

    @Override
    protected void onPostExecute(WeatherData weatherData) {
        if (weatherData != null) {
            listener.onWeatherFetched(weatherData);
        } else {
            Log.e(TAG, "No weather data fetched");
        }
    }

    private WeatherData parseWeatherData(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        Element root = doc.getDocumentElement();

        WeatherData weatherData = new WeatherData();

        NodeList conditionList = root.getElementsByTagName("aws:current-condition");
        if (conditionList.getLength() > 0) {
            Element conditionElement = (Element) conditionList.item(0);
            weatherData.setCurrentCondition(conditionElement.getTextContent());
            weatherData.setIconUrl(conditionElement.getAttribute("icon"));
        }

        NodeList feelsLikeList = root.getElementsByTagName("aws:feels-like");
        if (feelsLikeList.getLength() > 0) {
            weatherData.setFeelsLike(feelsLikeList.item(0).getTextContent());
        }

        NodeList tempList = root.getElementsByTagName("aws:temp");
        if (tempList.getLength() > 0) {
            weatherData.setTemperature(tempList.item(0).getTextContent());
        }

        NodeList windSpeedList = root.getElementsByTagName("aws:wind-speed");
        if (windSpeedList.getLength() > 0) {
            weatherData.setWindSpeed(windSpeedList.item(0).getTextContent());
        }

        // observation date / time
        weatherData.setObservationDateTime();

        return weatherData;
    }

    public interface WeatherListener {
        void onWeatherFetched(WeatherData weatherData);
    }
}
