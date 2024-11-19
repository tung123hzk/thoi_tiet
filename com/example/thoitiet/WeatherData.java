package com.example.thoitiet;

public class WeatherData {
    private String time;
    private double temperature;
    private int humidity;
    private double windSpeed;
    private String weatherCondition;

    public WeatherData(String time, double temperature, int humidity, double windSpeed, String weatherCondition) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherCondition = weatherCondition;
    }

    public String getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }
}
