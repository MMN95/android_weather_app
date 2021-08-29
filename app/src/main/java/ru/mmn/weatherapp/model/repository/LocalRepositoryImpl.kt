package ru.mmn.weatherapp.model.repository

import ru.mmn.weatherapp.model.data.Weather
import ru.mmn.weatherapp.model.data.convertHistoryEntityToWeather
import ru.mmn.weatherapp.model.data.convertWeatherToEntity
import ru.mmn.weatherapp.room.HistoryDao

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        return localDataSource.insert(convertWeatherToEntity(weather))
    }
}