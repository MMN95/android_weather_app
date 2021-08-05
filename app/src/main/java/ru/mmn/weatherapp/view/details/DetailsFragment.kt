/*package ru.mmn.weatherapp.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.mmn.weatherapp.viewmodel.AppState
import ru.mmn.weatherapp.R
import ru.mmn.weatherapp.model.Weather
import ru.mmn.weatherapp.databinding.FragmentDetailsBinding
import ru.mmn.weatherapp.viewmodel.MainViewModel

class DetailsFragment : Fragment() {

    private var binding: FragmentDetailsBinding? = null
    private val getBind get() = binding!!
    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = getBind.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it)})
        viewModel.getWeatherFromLocalSourceRus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                getBind.loadingLayout.visibility = View.GONE
                setData(weatherData)
            }

            is AppState.Loading -> {
                getBind.loadingLayout.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                getBind.loadingLayout.visibility = View.GONE
                Snackbar.make(getBind.mainView, "Ошибка", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Перезагрузка") { viewModel.getWeatherFromLocalSourceRus()}
                        .show()

            }
        }
    }

    private fun setData(weatherData: Weather) {
        getBind.cityName.text = weatherData.city.city
        getBind.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weatherData.city.lat.toString(),
                weatherData.city.lon.toString()
        )
        getBind.temperatureValue.text = weatherData.temperature.toString()
        getBind.feelsLikeValue.text = weatherData.feelsLike.toString()
    }

}
 */