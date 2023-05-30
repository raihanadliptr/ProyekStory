package com.example.mystoryproject.utils

import com.example.mystoryproject.model.StoryModel

object DummyData {
    fun generateDummyStories(): List<StoryModel> {
        val stories = ArrayList<StoryModel>()
        for (i in 0..15) {
            val story = StoryModel(
                i.toString(),
                "created + $i",
                "name + $i",
                "description + $i",
                i.toString(),
                "id + $i",
                i.toString(),
            )
            stories.add(story)
        }
        return stories
    }
}