package com.capstone.catarak.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.capstone.catarak.data.response.StoryResponse
import com.capstone.catarak.domain.entity.StoryEntity
import com.capstone.catarak.domain.mapper.map
import com.capstone.catarak.fake.FakeGetStoriesUseCase
import com.capstone.catarak.fake.FakeGetUserUseCase
import com.capstone.catarak.fake.FakeLogoutUseCase
import com.capstone.catarak.ui.adapter.StoryAdapter
import com.capstone.catarak.ui.story.StoryViewModel
import com.capstone.catarak.utils.DataDummy
import com.capstone.catarak.utils.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    private val getStoriesUseCase = FakeGetStoriesUseCase()

    private val getUserUseCase = FakeGetUserUseCase()

    private val getLogoutUseCase = FakeLogoutUseCase()


    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        //Given
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<StoryResponse> = StoryPagingSource.snapshot(dummyStory)
        val storyViewModel = StoryViewModel(getStoriesUseCase, getUserUseCase, getLogoutUseCase)
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )


        //When
        storyViewModel.getStories()
        getStoriesUseCase.fakeDelegate.emit(data.map())
        differ.submitData(storyViewModel.storyState.value.resultStories)

        //Then
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory.map().first(), differ.snapshot().first())
        // Checking the type of both expected and actual data isn't possible in my code, because a mapping process in domain layer.
        // Workaround I mapping it first to from StoryResponse into StoryEntity

    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {

        //Given
        val data: PagingData<StoryEntity> = PagingData.empty()
        val storyViewModel = StoryViewModel(getStoriesUseCase, getUserUseCase, getLogoutUseCase)
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        //When
        storyViewModel.getStories()
        getStoriesUseCase.fakeDelegate.emit(data)
        differ.submitData(storyViewModel.storyState.value.resultStories)

        //Then
        Assert.assertEquals(0, differ.snapshot().size)
    }

}


class StoryPagingSource : PagingSource<Int, LiveData<List<StoryResponse>>>() {
    companion object {
        fun snapshot(items: List<StoryResponse>): PagingData<StoryResponse> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryResponse>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryResponse>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}


val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}