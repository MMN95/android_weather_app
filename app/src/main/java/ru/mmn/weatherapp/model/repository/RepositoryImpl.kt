package ru.mmn.weatherapp.model.repository

import ru.mmn.weatherapp.model.data.Weather
import ru.mmn.weatherapp.model.data.getRussianCities
import ru.mmn.weatherapp.model.data.getWorldCities


class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}