package com.example.mystoryproject.model

import com.google.gson.annotations.SerializedName

data class ListStory(
    @field:SerializedName("listStory")
    val listStory: List<StoryModel>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: Boolean,
)