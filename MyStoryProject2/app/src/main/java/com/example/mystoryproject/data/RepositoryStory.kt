package com.example.mystoryproject.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryproject.model.StoryModel
import com.example.mystoryproject.ui.remote.ApiService

class RepositoryStory(
    private val service: ApiService,
    private val token: String) {

        fun getStory(): LiveData<PagingData<StoryModel>> {
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                pagingSourceFactory = {
                    PagingSource(service, token)
                }
            ).liveData
        }
}