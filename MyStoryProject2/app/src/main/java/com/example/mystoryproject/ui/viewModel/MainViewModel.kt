package com.example.mystoryproject.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryproject.data.RepositoryStory
import com.example.mystoryproject.model.ListStory
import com.example.mystoryproject.model.StoryModel
import com.example.mystoryproject.ui.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: RepositoryStory) : ViewModel() {
    companion object{
        private const val TAG = "MainViewModel"
    }

    var story: LiveData<PagingData<StoryModel>> =
        repository.getStory().cachedIn(viewModelScope)

    fun refreshData() {
        story = repository.getStory().cachedIn(viewModelScope)
    }
}