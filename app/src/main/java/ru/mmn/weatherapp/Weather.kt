package ru.mmn.weatherapp

data class Weather (val city: City = getDefaultCity(), val temperature: Int = 0, val feelsLike: Int = 0)

fun getDefaultCity(): City {
    return City("Yekaterinburg", 56.51, 60.36)
}

