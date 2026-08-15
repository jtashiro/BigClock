package com.fiospace.bigclock.datasources.homebrew;

import java.util.ArrayList;
import java.util.List;

public class ForecastData {
    private List<ForecastEntry> forecastEntries ;
    private Location location ;


    public ForecastData() {
        this.location = new Location();
        this.forecastEntries = new ArrayList<>();
    }

    public List<ForecastEntry> getForecastEntries() {
        return forecastEntries;
    }

    public void addForecastEntry(ForecastEntry forecastEntry) {
        forecastEntries.add(forecastEntry);
    }
    public ForecastEntry getForecastEntryByIndex(int index) {
        if (index >= 0 && index < forecastEntries.size()) {
            return forecastEntries.get(index);
        } else {
            return null;
        }
    }
    public void clearForecastEntries() {
        forecastEntries.clear();
    }

    public static class ForecastEntry {
        private String title = new String();
        private String high = new String();
        private String low = new String();
        private String description = new String();
        private String shortPrediction = new String();
        private String imageUrl = new String();
        private String prediction = new String();
        private String sunriseTimeLocal = new String();
        private String sunsetTimeLocal  = new String();
        private String relativeHumidity = new String();

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getShortPrediction() {
            return shortPrediction;
        }

        public void setShortPrediction(String shortPrediction) {
            this.shortPrediction = shortPrediction;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getPrediction() {
            return prediction;
        }

        public void setPrediction(String prediction) {
            this.prediction = prediction;
        }

        public String getSunriseTimeLocal() { return this.sunriseTimeLocal; }

        public void setSunriseTimeLocal(String sunriseTimeLocal) {
            this.sunriseTimeLocal = sunriseTimeLocal;
        }

        public String getSunsetTimeLocal() { return this.sunsetTimeLocal; }

        public void setSunsetTimeLocal(String sunsetTimeLocal) {
            this.sunsetTimeLocal = sunsetTimeLocal;
        }

        public String getRelativeHumidity() {
            return relativeHumidity;
        }

        public void setRelativeHumidity(String relativeHumidity) {
            this.relativeHumidity = relativeHumidity;
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // Inner class for location data
    public static class Location {
        private String city = new String();
        private String state = new String();
        private String zip = new String();
        private String zone = new String();

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }
    }

}
