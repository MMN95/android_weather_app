package ru.mmn.weatherapp.view.main

import DetailsFragment
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

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var isDataSetRus: Boolean = true

    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                        .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                            putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                        }))
                        .addToBackStack("")
                        .commitAllowingStateLoss()
            }
        }
    })


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(android.R.drawable.ic_popup_sync)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(android.R.drawable.ic_popup_sync)
        }.also { isDataSetRus = !isDataSetRus }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.hide()
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.show()
            }
            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.mainFragmentFAB.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getWeatherFromLocalSourceRus() }
                )
            }
        }
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    companion object {
        fun newInstance() =
                MainFragment()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    private fun View.showSnackBar(
            text: String,
            actionText: String,
            action: (View) -> Unit,
            length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    private fun View.show(): View {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        return this
    }

    private fun View.hide(): View {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
        return this
    }

}

