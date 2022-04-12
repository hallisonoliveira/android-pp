package com.picpay.desafio.android.usecase

import com.picpay.desafio.android.model.domain.User

interface FetchUsersUseCase {
    suspend fun execute(): List<User>
}