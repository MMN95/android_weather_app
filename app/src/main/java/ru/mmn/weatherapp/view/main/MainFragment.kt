package ru.mmn.weatherapp.view.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.mmn.weatherapp.R
import ru.mmn.weatherapp.databinding.MainFragmentBinding
import ru.mmn.weatherapp.model.AppState
import ru.mmn.weatherapp.model.data.City
import ru.mmn.weatherapp.model.data.Weather
import ru.mmn.weatherapp.view.DetailsFragment
import ru.mmn.weatherapp.view.hide
import ru.mmn.weatherapp.view.show
import ru.mmn.weatherapp.view.showSnackBar
import ru.mmn.weatherapp.viewmodel.MainViewModel
import java.io.IOException

private const val IS_RUSSIAN_KEY = "LIST_OF_RUSSIAN_KEY"
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MainFragment : Fragment() {


    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var _binding: MainFragmentBinding? = null
    private val binding
        get() = _binding!!
    private val adapter = MainFragmentAdapter()
    private var isDataSetRus: Boolean = true
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.setOnItemViewClickListener { weather ->
            openDetailsFragment(weather)
        }
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener {
            changeWeatherDataSet()
            saveListOfTowns()
        }

        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
        val observer = Observer<AppState> { a ->
            renderData(a)
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        loadListOfTowns()
        showWeatherDataSet()
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    getLocation()
                } else {
                    showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                    )
                }
            }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(getString(R.string.dialog_rationale_title))
                    .setMessage(getString(R.string.dialog_rationale_message))
                    .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                        requestPermission()
                    }
                    .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
        }
    }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                REFRESH_PERIOD,
                                MINIMAL_DISTANCE,
                                onLocationListener
                        )
                    }
                } else {
                    val location =
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                                getString(R.string.dialog_title_gps_turned_off),
                                getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddress(context, location)
                        showDialog(
                                getString(R.string.dialog_title_gps_turned_off),
                                getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddress(it, location)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun getAddress(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 5)
                binding.mainFragmentFAB.post {
                    showAddressDialog(addresses.first().getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(getString(R.string.dialog_address_title))
                    .setMessage(address)
                    .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                        openDetailsFragment(
                                Weather(
                                        City(
                                                address,
                                                location.latitude,
                                                location.longitude
                                        )
                                )
                        )
                    }
                    .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
        }
    }

    private fun openDetailsFragment(weather: Weather) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
        }
    }

    private fun loadListOfTowns() {
        requireActivity().apply {
            isDataSetRus = getPreferences(Context.MODE_PRIVATE).getBoolean(IS_RUSSIAN_KEY, true)
        }
    }

    private fun saveListOfTowns() {
        requireActivity().apply {
            getPreferences(Context.MODE_PRIVATE).edit {
                putBoolean(IS_RUSSIAN_KEY, isDataSetRus)
                apply()
            }
        }
    }

    private fun changeWeatherDataSet() {
        isDataSetRus = !isDataSetRus
        showWeatherDataSet()
    }

    private fun showWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(android.R.drawable.ic_popup_sync)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(android.R.drawable.ic_popup_sync)
        }
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success -> {
                binding.loadingLayout.hide()
                adapter.setWeather(data.weatherData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.show()
            }
            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.mainFragmentFAB.showSnackBar("Error", "Reload") {
                    if (isDataSetRus) viewModel.getWeatherFromLocalSourceRus()
                    else viewModel.getWeatherFromLocalSourceWorld()
                }
            }
        }
    }
}

