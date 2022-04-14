package com.picpay.desafio.android.usecase

import com.picpay.desafio.android.model.domain.User
import kotlinx.coroutines.flow.Flow

interface FetchUsersUseCase {
    suspend fun execute(): Flow<List<User>>
}