package com.mohithasan.journalapp.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeatherResponse{

    private Current current;

    @Getter
    @Setter
    public static class Current{
        @JsonProperty("observation_time")
        private String observationTime;
        private int temperature;
        @JsonProperty("weather_code")
        private int weatherCode;
        @JsonProperty("wind_speed")
        private int windSpeed;
        private int humidity;
        @JsonProperty("feelslike")
        private int feelsLike;
        @JsonProperty("is_day")
        private String isDay;
    }
}



