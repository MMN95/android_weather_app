package ru.mmn.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mmn.weatherapp.app.App.Companion.getHistoryDao
import ru.mmn.weatherapp.model.AppState
import ru.mmn.weatherapp.model.repository.LocalRepository
import ru.mmn.weatherapp.model.repository.LocalRepositoryImpl

class HistoryViewModel(
        val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
        private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {

    fun getAllHistory(){
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.Success(historyRepository.getAllHistory())
    }
}