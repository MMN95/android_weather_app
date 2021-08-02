package ru.mmn.weatherapp

interface Repository {
   fun getWeatherFromServer(): Weather
   fun getWeatherFromLocalStorage(): Weather

}