package ru.mmn.weatherapp

data class Weather (private var town:String, private var temperature:Int)

val ekbWeather = Weather("EKB", 25)