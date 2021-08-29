package ru.mmn.weatherapp.model.repository

import ru.mmn.weatherapp.model.data.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}