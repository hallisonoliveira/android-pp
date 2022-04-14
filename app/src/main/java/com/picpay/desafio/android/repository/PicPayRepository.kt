package com.picpay.desafio.android.repository

import com.picpay.desafio.android.model.domain.User
import kotlinx.coroutines.flow.Flow

interface PicPayRepository {
    suspend fun fetchUsers(): Flow<List<User>>
}