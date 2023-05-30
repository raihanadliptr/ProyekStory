package com.example.mystoryproject.data

import android.content.Context
import com.example.mystoryproject.ui.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context, token : String): RepositoryStory {
        val service = ApiConfig.getApiService()
        return RepositoryStory(service, token)
    }
}