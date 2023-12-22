package com.capstone.catarak.fake

import com.capstone.catarak.domain.contract.LogoutUseCaseContract
class FakeLogoutUseCase : LogoutUseCaseContract {


    override suspend fun invoke() = Unit


}