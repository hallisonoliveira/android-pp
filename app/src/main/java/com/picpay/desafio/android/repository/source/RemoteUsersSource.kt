package com.picpay.desafio.android.repository.source

import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.model.mapper.toDomain
import com.picpay.desafio.android.service.PicPayService

interface RemoteUsersSource {
    suspend fun fetch(): List<User>
}

internal class RemoteUsersSourceImpl constructor(
    private val service: PicPayService
) : RemoteUsersSource {

    override suspend fun fetch(): List<User> {
        return service.fetchUsers().map { it.toDomain() }
    }
}