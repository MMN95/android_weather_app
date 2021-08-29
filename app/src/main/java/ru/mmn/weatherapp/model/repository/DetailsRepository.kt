package ru.mmn.weatherapp.model.repository

import ru.mmn.weatherapp.model.dto.WeatherDTO

interface DetailsRepository {
    fun getWeatherDetailsFromServer(
            lat: Double,
            lon: Double,
            callback: retrofit2.Callback<WeatherDTO>
    )
}