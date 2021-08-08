package ru.mmn.weatherapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mmn.weatherapp.R
import ru.mmn.weatherapp.model.Weather
import ru.mmn.weatherapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var binding: FragmentDetailsBinding? = null
    private val getBind get() = binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return getBind.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)
        if (weather != null) {
            val city = weather.city
            getBind.cityName.text = city.city
            getBind.cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    city.lat.toString(),
                    city.lon.toString()
            )
            getBind.temperatureValue.text = weather.temperature.toString()
            getBind.feelsLikeValue.text = weather.feelsLike.toString()
        }
    }

    companion object {

        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
