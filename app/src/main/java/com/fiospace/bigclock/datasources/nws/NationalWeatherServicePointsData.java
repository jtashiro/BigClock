package com.fiospace.bigclock.datasources.nws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class NationalWeatherServicePointsData {
    @SerializedName("@id")
    private String id;
    private String type;
    private double latitude;
    private double longitude;
    private String cwa;
    @SerializedName("forecastOffice")
    private String forecastOfficeUrl;
    private String gridId;
    private int gridX;
    private int gridY;
    private String forecast;
    @SerializedName("forecastHourly")
    private String forecastHourlyUrl;
    @SerializedName("forecastGridData")
    private String forecastGridDataUrl;
    @SerializedName("observationStations")
    private String observationStationsUrl;
    @SerializedName("relativeLocation")
    private RelativeLocation relativeLocation;
    @SerializedName("forecastZone")
    private String forecastZoneUrl;
    private String county;
    @SerializedName("fireWeatherZone")
    private String fireWeatherZoneUrl;
    @SerializedName("timeZone")
    private String timeZone;
    @SerializedName("radarStation")
    private String radarStation;

    private static JsonObject jsonObject;
    private static Gson gson;

    // Constructor, getters, and setters
    public  NationalWeatherServicePointsData(String weatherData) {
        if (weatherData != null) {
            gson = new Gson();
            //fromJson(weatherData);
            jsonObject = gson.fromJson(weatherData, JsonObject.class);
            if (jsonObject != null) {
                setForecastOfficeUrl(getPropertyString("forecast"));
            }
        }
    }

    private String getPropertyString(String key) {
        if (jsonObject != null) {
            return jsonObject.getAsJsonObject("properties").get( key ).getAsString();
        } else {
            return null;
        }
    }

    public static NationalWeatherServicePointsData XfromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, NationalWeatherServicePointsData.class);
    }

    public void fromJson(String json) {
        Gson gson = new Gson();
        jsonObject = gson.fromJson(json, JsonObject.class);

    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCwa() {
        return cwa;
    }

    public void setCwa(String cwa) {
        this.cwa = cwa;
    }

    public String getForecastOfficeUrl() {
        return forecastOfficeUrl;
    }

    public void setForecastOfficeUrl(String forecastOfficeUrl) {
        this.forecastOfficeUrl = forecastOfficeUrl;
    }

    public String getGridId() {
        forecastOfficeUrl = jsonObject.getAsJsonObject("properties").get("gridId").getAsString();

        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public String getForecastHourlyUrl() {
        return forecastHourlyUrl;
    }

    public void setForecastHourlyUrl(String forecastHourlyUrl) {
        this.forecastHourlyUrl = forecastHourlyUrl;
    }

    public String getForecastGridDataUrl() {
        return forecastGridDataUrl;
    }

    public void setForecastGridDataUrl(String forecastGridDataUrl) {
        this.forecastGridDataUrl = forecastGridDataUrl;
    }

    public String getObservationStationsUrl() {
        return observationStationsUrl;
    }

    public void setObservationStationsUrl(String observationStationsUrl) {
        this.observationStationsUrl = observationStationsUrl;
    }

    public RelativeLocation getRelativeLocation() {
        return relativeLocation;
    }

    public void setRelativeLocation(RelativeLocation relativeLocation) {
        this.relativeLocation = relativeLocation;
    }

    public String getForecastZoneUrl() {
        return forecastZoneUrl;
    }

    public void setForecastZoneUrl(String forecastZoneUrl) {
        this.forecastZoneUrl = forecastZoneUrl;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getFireWeatherZoneUrl() {
        return fireWeatherZoneUrl;
    }

    public void setFireWeatherZoneUrl(String fireWeatherZoneUrl) {
        this.fireWeatherZoneUrl = fireWeatherZoneUrl;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getRadarStation() {
        return radarStation;
    }

    public void setRadarStation(String radarStation) {
        this.radarStation = radarStation;
    }

    // Nested class for relative location
    static class RelativeLocation {
        private Properties properties;

        static class Properties {
            private String city;
            private String state;
            private Distance distance;
            private Bearing bearing;

            static class Distance {
                private double value;
                private String unitCode;

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }

                public String getUnitCode() {
                    return unitCode;
                }

                public void setUnitCode(String unitCode) {
                    this.unitCode = unitCode;
                }
            }

            static class Bearing {
                private double value;
                private String unitCode;

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }

                public String getUnitCode() {
                    return unitCode;
                }

                public void setUnitCode(String unitCode) {
                    this.unitCode = unitCode;
                }
            }

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

            public Distance getDistance() {
                return distance;
            }

            public void setDistance(Distance distance) {
                this.distance = distance;
            }

            public Bearing getBearing() {
                return bearing;
            }

            public void setBearing(Bearing bearing) {
                this.bearing = bearing;
            }
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }
}
