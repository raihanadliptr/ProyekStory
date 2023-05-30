package com.example.mystoryproject.model

import com.google.gson.annotations.SerializedName

data class DetailStory(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("story")
    val story: StoryModel,
)