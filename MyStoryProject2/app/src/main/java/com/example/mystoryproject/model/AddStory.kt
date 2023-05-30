package com.example.mystoryproject.model

import com.google.gson.annotations.SerializedName

data class AddStory(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)