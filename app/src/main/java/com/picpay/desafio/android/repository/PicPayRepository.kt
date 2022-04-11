package com.picpay.desafio.android.repository

import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.model.mapper.toDomain
import com.picpay.desafio.android.service.PicPayService
import javax.inject.Inject

interface PicPayRepository {

    suspend fun fetchUsers(): List<User>

}

internal class PicPayRepositoryImpl @Inject constructor(
    private val service: PicPayService
) : PicPayRepository {

    override suspend fun fetchUsers(): List<User> {
        return service.fetchUsers().map { it.toDomain() }
    }

}