package com.example.mystoryproject.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryproject.R
import com.example.mystoryproject.databinding.ActivityMainBinding
import com.example.mystoryproject.model.StoryModel
import com.example.mystoryproject.ui.authentication.AuthenticationActivity
import com.example.mystoryproject.ui.maps.MapsActivity
import com.example.mystoryproject.ui.upload.UploadActivity
import com.example.mystoryproject.ui.viewModel.*
import com.example.mystoryproject.utils.SettingsPreferences

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private fun showLoading(isLoading: Boolean) {
        if (!isLoading) {
            binding.progressBar.visibility = View.GONE
        }
        else {
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun playAnimation() {
        val animator = ObjectAnimator.ofFloat(binding.fab, View.ALPHA, 1f).setDuration(400)
        AnimatorSet().apply {
            playSequentially(animator)
            start()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        val previous = SettingsPreferences.getInstance(dataStore)
        val settingsVm = ViewModelProvider(this, SettingsViewModelFactory(previous)).get(SettingsViewModel::class.java)
        var token: String

        settingsVm.getToken().observe(this) {
            token = "Bearer $it"
            val mainVm by viewModels<MainViewModel>(){
                MainViewModelFactory(this, token)
            }
            val adapter = Adapter()

            binding.storiesRv.adapter = adapter.withLoadStateFooter(
                footer = LoadingAdapter {
                    adapter.retry()
                }
            )
            mainVm.refreshData()
            mainVm.story.observe(this) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layout = LinearLayoutManager(this)
        binding.storiesRv.layoutManager = layout
        val decoration = DividerItemDecoration(this, layout.orientation)
        binding.storiesRv.addItemDecoration(decoration)

        val previous = SettingsPreferences.getInstance(dataStore)
        val settingsVm = ViewModelProvider(this, SettingsViewModelFactory(previous)).get(SettingsViewModel::class.java)
        var token: String

        settingsVm.getToken().observe(this) {
            if (it == "Not Set") {
                val details = Intent(this, AuthenticationActivity::class.java)
                startActivity(details)
            }

            token = "Bearer $it"
            val mainVm by viewModels<MainViewModel>(){
                MainViewModelFactory(this, token)
            }

            val adapter = Adapter()

            binding.storiesRv.adapter = adapter.withLoadStateFooter(
                footer = LoadingAdapter {
                    adapter.retry()
                }
            )
            mainVm.story.observe(this) {
                adapter.submitData(lifecycle, it)
            }
        }

        binding.fab.setOnClickListener {
            val details = Intent(this, UploadActivity::class.java)
            startActivity(details)
        }

        playAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                val previous = SettingsPreferences.getInstance(dataStore)
                val settingsVm = ViewModelProvider(this, SettingsViewModelFactory(previous)).get(SettingsViewModel::class.java)
                settingsVm.clearUserPreferences()
                val details = Intent(this, AuthenticationActivity::class.java)
                startActivity(details)
                return true
            }
            R.id.maps -> {
                val intentDetail = Intent(this, MapsActivity::class.java)
                startActivity(intentDetail)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}