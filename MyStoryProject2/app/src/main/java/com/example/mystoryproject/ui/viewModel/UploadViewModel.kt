package com.example.mystoryproject.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryproject.model.AddStory
import com.example.mystoryproject.ui.remote.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(token: String) : ViewModel() {
    private var token = token
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _response = MutableLiveData<AddStory>()
    val response: LiveData<AddStory> = _response

    companion object{
        private const val TAG = "DetailViewModel"
    }

    fun uploadStory(description: RequestBody, multipart: MultipartBody.Part) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadStory(token, multipart, description)
        client.enqueue(object : Callback<AddStory> {
            override fun onResponse(call: Call<AddStory>, response: Response<AddStory>) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    Log.e(TAG, "onFailure: ${response.message()}")
                } else {
                    _response.value = response.body()
                }
            }
            override fun onFailure(call: Call<AddStory>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}