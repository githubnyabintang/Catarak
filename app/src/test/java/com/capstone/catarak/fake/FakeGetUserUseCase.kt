package com.capstone.catarak.fake

import com.capstone.catarak.domain.contract.GetUserUseCaseContract
import com.capstone.catarak.domain.entity.UserEntity
import com.capstone.catarak.utils.FakeFlowDelegate
import kotlinx.coroutines.flow.Flow

class FakeGetUserUseCase : GetUserUseCaseContract {

    val fakeDelegate = FakeFlowDelegate<UserEntity>()

    override fun invoke(): Flow<UserEntity> = fakeDelegate.flow

}