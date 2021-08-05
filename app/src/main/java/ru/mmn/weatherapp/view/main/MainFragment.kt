package ru.mmn.weatherapp.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.mmn.weatherapp.R
import ru.mmn.weatherapp.databinding.FragmentMainBinding
import ru.mmn.weatherapp.model.Weather
import ru.mmn.weatherapp.viewmodel.AppState
import ru.mmn.weatherapp.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private val getBind get() = binding!!

    private lateinit var viewModel: MainViewModel
    private val adapter = MainFragmentAdapter()
    private var isDataSetRus: Boolean = true

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return getBind.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBind.mainFragmentRecyclerView.adapter = adapter
        getBind.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            getBind.mainFragmentFAB.setImageResource(android.R.drawable.ic_popup_sync)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            getBind.mainFragmentFAB.setImageResource(android.R.drawable.ic_popup_sync)
        }
        isDataSetRus = !isDataSetRus
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                getBind.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                getBind.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                getBind.mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar
                        .make(getBind.mainFragmentFAB, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.reload)) {
                            viewModel.getWeatherFromLocalSourceRus() }
                        .show()
            }
        }
    }

    companion object {
        fun newInstance() =
                MainFragment()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }
}

