package com.picpay.desafio.android.usecase.impl

import com.picpay.desafio.android.repository.PicPayRepository
import com.picpay.desafio.android.usecase.FetchUsersUseCase

internal class FetchUsersUseCaseImpl constructor(
    private val repository: PicPayRepository
) : FetchUsersUseCase {

    override suspend fun execute() = repository.fetchUsers()

}