package com.mohithasan.journalapp.service;


import com.mohithasan.journalapp.api.response.WeatherResponse;
import com.mohithasan.journalapp.cache.AppCache;
import com.mohithasan.journalapp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final AppCache appCache;
    private final RedisService redisService;

    @Autowired
    public WeatherService(RestTemplate restTemplate, AppCache appCache, RedisService redisService){
        this.restTemplate = restTemplate;
        this.appCache = appCache;
        this.redisService = redisService;
    }

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse != null){
            return weatherResponse;
        }
        String finalAPI = appCache.getCache().get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.API_KEY, apiKey).replace(Placeholders.CITY, city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        if (body != null){
            redisService.set("weather_of_"+city, body, 300L);
        }

        return body;
    }
}
