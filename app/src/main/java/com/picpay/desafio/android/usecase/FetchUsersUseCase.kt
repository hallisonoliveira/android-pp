package com.picpay.desafio.android.usecase

import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.repository.PicPayRepository
import javax.inject.Inject

interface FetchUsersUseCase {
    suspend fun execute(): List<User>
}

internal class FetchUsersUseCaseImpl constructor(
    private val repository: PicPayRepository
) : FetchUsersUseCase {

    override suspend fun execute(): List<User> {
        return repository.fetchUsers()
    }

}