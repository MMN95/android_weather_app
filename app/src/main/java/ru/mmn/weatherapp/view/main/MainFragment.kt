package ru.mmn.weatherapp.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.mmn.weatherapp.R
import ru.mmn.weatherapp.databinding.MainFragmentBinding
import ru.mmn.weatherapp.model.AppState
import ru.mmn.weatherapp.view.DetailsFragment
import ru.mmn.weatherapp.view.hide
import ru.mmn.weatherapp.view.show
import ru.mmn.weatherapp.view.showSnackBar
import ru.mmn.weatherapp.viewmodel.MainViewModel

private const val IS_RUSSIAN_KEY = "LIST_OF_RUSSIAN_KEY"

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
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                        .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                            putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                        }))
                        .addToBackStack("")
                        .commitAllowingStateLoss()
            }
        }
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener {
            changeWeatherDataSet()
            saveListOfTowns()
        }
        val observer = Observer<AppState> { a ->
            renderData(a)
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        loadListOfTowns()
        showWeatherDataSet()
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

