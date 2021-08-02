package ru.mmn.weatherapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mmn.weatherapp.AppState
import ru.mmn.weatherapp.Repository
import ru.mmn.weatherapp.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel (
        private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
        private val repositoryImpl: Repository = RepositoryImpl()) :
        ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSource() = getDataFromLocalSource()

    //TODO fun getWeatherFromRemoteSource() = getWeatherFromRemoteSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorage()))
        }.start()
    }
}