package com.capstone.catarak.fake

import androidx.paging.PagingData
import com.capstone.catarak.domain.contract.GetStoriesUseCaseContract
import com.capstone.catarak.domain.entity.StoryEntity
import com.capstone.catarak.utils.FakeFlowDelegate
import kotlinx.coroutines.flow.Flow

class FakeGetStoriesUseCase : GetStoriesUseCaseContract {

    val fakeDelegate = FakeFlowDelegate<PagingData<StoryEntity>>()

    override fun invoke(): Flow<PagingData<StoryEntity>> = fakeDelegate.flow

}