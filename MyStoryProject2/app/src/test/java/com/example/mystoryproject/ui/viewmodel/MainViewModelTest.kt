package com.example.mystoryproject.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.mystoryproject.data.RepositoryStory
import com.example.mystoryproject.model.StoryModel
import com.example.mystoryproject.ui.home.Adapter
import com.example.mystoryproject.ui.viewModel.MainViewModel
import com.example.mystoryproject.utils.DummyData
import com.example.mystoryproject.utils.MainDispatcherRule
import com.example.mystoryproject.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: RepositoryStory

    private val dummyStoriesResponse = DummyData.generateDummyStories()

    @Test
    fun `when Get Stories Returns Not Null`() = runTest {
        val data: PagingData<StoryModel> = StoryPagingSource.snapshot(dummyStoriesResponse)
        val expected = MutableLiveData<PagingData<StoryModel>>()
        expected.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(expected)

        val mainViewModel = MainViewModel(storyRepository)
        val actual: PagingData<StoryModel> = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = Adapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actual)

        Assert.assertNotNull(differ.snapshot())
        assertEquals(dummyStoriesResponse, differ.snapshot())
        assertEquals(dummyStoriesResponse.size, differ.snapshot().size)
        assertEquals(dummyStoriesResponse[0], differ.snapshot()[0])
    }

    private val nullStoriesResponse = ArrayList<StoryModel>()

    @Test
    fun `when Get Stories Returns Null`() = runTest {
        val data: PagingData<StoryModel> = StoryPagingSource.snapshot(ArrayList<StoryModel>())
        val expected = MutableLiveData<PagingData<StoryModel>>()
        expected.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(expected)

        val mainViewModel = MainViewModel(storyRepository)
        val actual: PagingData<StoryModel> = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = Adapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actual)

        Assert.assertNotNull(differ.snapshot())
        assertEquals(nullStoriesResponse, differ.snapshot())
        assertEquals(nullStoriesResponse.size, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryModel>>>() {
    companion object {
        fun snapshot(items: List<StoryModel>): PagingData<StoryModel> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryModel>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryModel>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}