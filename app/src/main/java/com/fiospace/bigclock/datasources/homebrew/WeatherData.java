package com.fiospace.bigclock.datasources.homebrew;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherData {
    private String currentCondition;
    private String iconUrl;
    private String feelsLike;
    private String temperature;
    private String windSpeed;
    private String location;
    private String observationDateTime;

    public WeatherData() {
        // Default constructor
        currentCondition = new String();
        iconUrl = new String();
        feelsLike = new String();
        temperature = new String();
        windSpeed = new String();
        location = new String();
        observationDateTime = new String();
    }

    public WeatherData(String currentCondition, String iconUrl, String feelsLike, String temperature, String windSpeed) {
        this.currentCondition = currentCondition;
        this.iconUrl = iconUrl;
        this.feelsLike = feelsLike;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    public String getObservationDateTime() { return observationDateTime; }

    public void setObservationDateTime() {
        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Print the current date and time
        // Print the current date and time in the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a");
        String formattedDateTime = currentDateTime.format(formatter);
        this.observationDateTime = formattedDateTime;

    }

    public String getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(String currentCondition) {
        this.currentCondition = currentCondition;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}

