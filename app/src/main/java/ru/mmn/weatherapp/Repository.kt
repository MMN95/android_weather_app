package ru.mmn.weatherapp

object Repository {
    private val weatherList: List<Weather>

    init {
        weatherList = listOf(ekbWeather, spbWeather, mskWeather)
    }

    fun getWeatherList(): List<Weather>{
        return weatherList
    }

}