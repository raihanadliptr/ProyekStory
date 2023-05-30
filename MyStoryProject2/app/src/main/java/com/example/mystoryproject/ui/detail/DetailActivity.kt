package com.example.mystoryproject.ui.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mystoryproject.R
import com.example.mystoryproject.databinding.ActivityDetailBinding
import com.example.mystoryproject.model.StoryModel
import com.example.mystoryproject.ui.viewModel.DetailViewModel
import com.example.mystoryproject.ui.viewModel.SettingsViewModel
import com.example.mystoryproject.ui.viewModel.SettingsViewModelFactory
import com.example.mystoryproject.ui.viewModel.ViewModelFactory
import com.example.mystoryproject.utils.SettingsPreferences

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {
        const val storyId = "id"
    }

    private fun setStoryData(details: StoryModel) {
        Glide.with(binding.root).load(details.photoUrl).into(binding.image)
        binding.username.text = details.name
        binding.description.text = details.description
    }

    private fun showLoading(isLoading: Boolean) {
        if (!isLoading) {
            binding.progressBar.visibility = View.GONE
        }
        else {
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val previous = SettingsPreferences.getInstance(dataStore)
        val settingsVm = ViewModelProvider(this, SettingsViewModelFactory(previous)).get(SettingsViewModel::class.java)
        val storyId = intent.getStringExtra(storyId)
        var token: String

        settingsVm.getToken().observe(this) {
            token = "Bearer $it"
            val detailVm by viewModels<DetailViewModel>(){ ViewModelFactory(token) }
            detailVm.isLoading.observe(this) { loading -> showLoading(loading) }

            if (storyId == null) {
                null
            }
            else {
                detailVm.getStory(storyId)
            }

            detailVm.details.observe(this) { details -> setStoryData(details.story) }
        }
    }
}