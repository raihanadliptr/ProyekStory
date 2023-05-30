package com.example.mystoryproject.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryproject.model.DetailStory
import com.example.mystoryproject.ui.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(token: String) : ViewModel() {
    private var token = token
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _details = MutableLiveData<DetailStory>()
    val details: LiveData<DetailStory> = _details

    companion object{
        private const val TAG = "DetailViewModel"
    }

    fun getStory(id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStoryDetail(token, id)
        client.enqueue(object : Callback<DetailStory> {
            override fun onResponse(call: Call<DetailStory>, response: Response<DetailStory>) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    Log.e(TAG, "onFailure: ${response.message()}")
                } else {
                    _details.value = response.body()
                }
            }
            override fun onFailure(call: Call<DetailStory>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}