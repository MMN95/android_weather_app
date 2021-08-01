package ru.mmn.weatherapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mmn.weatherapp.AppState
import java.lang.Thread.sleep

class MainViewModel (private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(AppState.Success(Any()))
        }.start()
    }
}