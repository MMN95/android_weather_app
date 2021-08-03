package ru.mmn.weatherapp.viewmodel

import ru.mmn.weatherapp.model.Weather

sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()

}