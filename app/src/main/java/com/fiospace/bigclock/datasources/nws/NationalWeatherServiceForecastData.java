package com.fiospace.bigclock.datasources.nws;

import com.google.gson.Gson;

import java.util.List;

public class NationalWeatherServiceForecastData {
    private List<Object> context;
    private String type;
    private Geometry geometry;
    private Properties properties;

    public List<Object> getContext() {
        return context;
    }

    public void setContext(List<Object> context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static NationalWeatherServiceForecastData fromJson(String jsonString) {
        return new Gson().fromJson(jsonString, NationalWeatherServiceForecastData.class);
    }

    public static class Geometry {
        private String type;
        private List<List<List<Double>>> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<List<List<Double>>> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<List<List<Double>>> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class Properties {
        private String updated;
        private String units;
        private String forecastGenerator;
        private String generatedAt;
        private String updateTime;
        private String validTimes;
        private Elevation elevation;
        private List<Period> periods;

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        public String getForecastGenerator() {
            return forecastGenerator;
        }

        public void setForecastGenerator(String forecastGenerator) {
            this.forecastGenerator = forecastGenerator;
        }

        public String getGeneratedAt() {
            return generatedAt;
        }

        public void setGeneratedAt(String generatedAt) {
            this.generatedAt = generatedAt;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getValidTimes() {
            return validTimes;
        }

        public void setValidTimes(String validTimes) {
            this.validTimes = validTimes;
        }

        public Elevation getElevation() {
            return elevation;
        }

        public void setElevation(Elevation elevation) {
            this.elevation = elevation;
        }

        public List<Period> getPeriods() {
            return periods;
        }

        public void setPeriods(List<Period> periods) {
            this.periods = periods;
        }

        public static class Elevation {
            private String unitCode;
            private Double value;

            public String getUnitCode() {
                return unitCode;
            }

            public void setUnitCode(String unitCode) {
                this.unitCode = unitCode;
            }

            public Double getValue() {
                return value;
            }

            public void setValue(Double value) {
                this.value = value;
            }
        }

        public static class Period {
            private Integer number;
            private String name;
            private String startTime;
            private String endTime;
            private Boolean isDaytime;
            private Integer temperature;
            private String temperatureUnit;
            private String temperatureTrend;
            private ProbabilityOfPrecipitation probabilityOfPrecipitation;
            private Dewpoint dewpoint;
            private RelativeHumidity relativeHumidity;
            private String windSpeed;
            private String windDirection;
            private String icon;
            private String shortForecast;
            private String detailedForecast;

            public Integer getNumber() {
                return number;
            }

            public void setNumber(Integer number) {
                this.number = number;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public Boolean getDaytime() {
                return isDaytime;
            }

            public void setDaytime(Boolean daytime) {
                isDaytime = daytime;
            }

            public Integer getTemperature() {
                return temperature;
            }

            public void setTemperature(Integer temperature) {
                this.temperature = temperature;
            }

            public String getTemperatureUnit() {
                return temperatureUnit;
            }

            public void setTemperatureUnit(String temperatureUnit) {
                this.temperatureUnit = temperatureUnit;
            }

            public String getTemperatureTrend() {
                return temperatureTrend;
            }

            public void setTemperatureTrend(String temperatureTrend) {
                this.temperatureTrend = temperatureTrend;
            }

            public ProbabilityOfPrecipitation getProbabilityOfPrecipitation() {
                return probabilityOfPrecipitation;
            }

            public void setProbabilityOfPrecipitation(ProbabilityOfPrecipitation probabilityOfPrecipitation) {
                this.probabilityOfPrecipitation = probabilityOfPrecipitation;
            }

            public Dewpoint getDewpoint() {
                return dewpoint;
            }

            public void setDewpoint(Dewpoint dewpoint) {
                this.dewpoint = dewpoint;
            }

            public RelativeHumidity getRelativeHumidity() {
                return relativeHumidity;
            }

            public void setRelativeHumidity(RelativeHumidity relativeHumidity) {
                this.relativeHumidity = relativeHumidity;
            }

            public String getWindSpeed() {
                return windSpeed;
            }

            public void setWindSpeed(String windSpeed) {
                this.windSpeed = windSpeed;
            }

            public String getWindDirection() {
                return windDirection;
            }

            public void setWindDirection(String windDirection) {
                this.windDirection = windDirection;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getShortForecast() {
                return shortForecast;
            }

            public void setShortForecast(String shortForecast) {
                this.shortForecast = shortForecast;
            }

            public String getDetailedForecast() {
                return detailedForecast;
            }

            public void setDetailedForecast(String detailedForecast) {
                this.detailedForecast = detailedForecast;
            }

            public static class ProbabilityOfPrecipitation {
                private String unitCode;
                private Integer value;

                public String getUnitCode() {
                    return unitCode;
                }

                public void setUnitCode(String unitCode) {
                    this.unitCode = unitCode;
                }

                public Integer getValue() {
                    return value;
                }

                public void setValue(Integer value) {
                    this.value = value;
                }
            }

            public static class Dewpoint {
                private String unitCode;
                private Double value;

                public String getUnitCode() {
                    return unitCode;
                }

                public void setUnitCode(String unitCode) {
                    this.unitCode = unitCode;
                }

                public Double getValue() {
                    return value;
                }

                public void setValue(Double value) {
                    this.value = value;
                }
            }

            public static class RelativeHumidity {
                private String unitCode;
                private Integer value;

                public String getUnitCode() {
                    return unitCode;
                }

                public void setUnitCode(String unitCode) {
                    this.unitCode = unitCode;
                }

                public Integer getValue() {
                    return value;
                }

                public void setValue(Integer value) {
                    this.value = value;
                }
            }
        }
    }
}
