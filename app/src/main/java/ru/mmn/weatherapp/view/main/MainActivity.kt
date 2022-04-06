package ru.mmn.weatherapp.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import ru.mmn.weatherapp.BuildConfig
import ru.mmn.weatherapp.googlemaps.GoogleMapsFragment
import ru.mmn.weatherapp.R
import ru.mmn.weatherapp.databinding.MainActivityBinding
import ru.mmn.weatherapp.view.HistoryFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, MainFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.apply {
                    beginTransaction()
                            .add(R.id.container, HistoryFragment.newInstance())
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                }
                true
            }
            R.id.menu_google_maps -> {
                if (BuildConfig.FLAVOR == "paidConfig") {
                    supportFragmentManager.apply {
                        beginTransaction()
                                .add(R.id.container, GoogleMapsFragment())
                                .addToBackStack("")
                                .commitAllowingStateLoss()
                    }
                }
                if (BuildConfig.FLAVOR == "freeConfig") {
                    Toast.makeText(this, "Эта функция доступна только в платной версии приложения", Toast.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}