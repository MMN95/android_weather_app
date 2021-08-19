package ru.mmn.weatherapp.repository

interface DetailsRepository {
    fun getWeatherDetailsFromServer(requestLink: String, callback: okhttp3.Callback)
}