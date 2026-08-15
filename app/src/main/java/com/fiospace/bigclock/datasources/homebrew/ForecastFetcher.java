package com.fiospace.bigclock.datasources.homebrew;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ForecastFetcher extends AsyncTask<Void, Void, ForecastData> {
    private static final String TAG = ForecastFetcher.class.getSimpleName();
    private final ForecastListener listener;
    private String datasource_hostname = "a5775249777.api.wxbug.net";
    String apiUrl = "http://" + datasource_hostname + "/getForecastRSS.aspx?ACode=A5775249777&zipCode=10308&UnitType=0&OutputType=0";

    public ForecastFetcher(ForecastListener listener) {
        this.listener = listener;
    }

    @Override
    protected ForecastData doInBackground(Void... voids) {
        ForecastData forecastData = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                forecastData = parseForecastData(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forecastData;
    }

    @Override
    protected void onPostExecute(ForecastData forecastData) {
        if (listener != null) {
            listener.onForecastFetched(forecastData);
        }
    }

    private ForecastData parseForecastData(InputStream inputStream) throws Exception {
        ForecastData forecastData = new ForecastData();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();

        // Parse location data
        Element locationElement = (Element) doc.getElementsByTagName("aws:location").item(0);
        ForecastData.Location location = new ForecastData.Location();
        location.setCity(locationElement.getElementsByTagName("aws:city").item(0).getTextContent());
        location.setState(locationElement.getElementsByTagName("aws:state").item(0).getTextContent());
        location.setZip(locationElement.getElementsByTagName("aws:zip").item(0).getTextContent());
        location.setZone(locationElement.getElementsByTagName("aws:zone").item(0).getTextContent());
        forecastData.setLocation(location);

        NodeList forecastNodeList = doc.getElementsByTagName("aws:forecast");

        for (int i = 0; i < forecastNodeList.getLength(); i++) {
            Element forecastElement = (Element) forecastNodeList.item(i);
            ForecastData.ForecastEntry forecastEntry = new ForecastData.ForecastEntry();

            forecastEntry.setTitle(forecastElement.getElementsByTagName("aws:title").item(0).getTextContent());
            forecastEntry.setHigh(forecastElement.getElementsByTagName("aws:high").item(0).getTextContent());
            forecastEntry.setLow(forecastElement.getElementsByTagName("aws:low").item(0).getTextContent());
            forecastEntry.setDescription(forecastElement.getElementsByTagName("aws:description").item(0).getTextContent());
            forecastEntry.setShortPrediction(forecastElement.getElementsByTagName("aws:short-prediction").item(0).getTextContent());
            forecastEntry.setImageUrl(forecastElement.getElementsByTagName("aws:image").item(0).getTextContent());
            forecastEntry.setPrediction(forecastElement.getElementsByTagName("aws:prediction").item(0).getTextContent());

            forecastData.addForecastEntry(forecastEntry);
        }
        return forecastData;
    }

    public interface ForecastListener {
        void onForecastFetched(ForecastData forecastData);
    }
}
