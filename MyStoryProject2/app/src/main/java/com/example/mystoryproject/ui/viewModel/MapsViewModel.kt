package com.example.mystoryproject.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryproject.model.ListStory
import com.example.mystoryproject.ui.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(token: String) : ViewModel() {
    private var token = token
    private val _stories = MutableLiveData<ListStory>()
    val stories: LiveData<ListStory> = _stories

    companion object{
        private const val TAG = "MainViewModel"
    }

    init {
        getLocationOfStories(200)
    }

    private fun getLocationOfStories(size: Int) {
        val client = ApiConfig.getApiService().getLocationOfStories(token, size)
        client.enqueue(object : Callback<ListStory> {
            override fun onResponse(call: Call<ListStory>, response: Response<ListStory>) {
                if (!response.isSuccessful) {
                    Log.e(TAG, "onFailure: ${response.message()}")
                } else {
                    _stories.value = response.body()
                }
            }
            override fun onFailure(call: Call<ListStory>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}