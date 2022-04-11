package com.picpay.desafio.android.repository

import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.model.mapper.toDomain
import com.picpay.desafio.android.service.PicPayService

class PicPayRepository constructor(
    private val service: PicPayService
) {

    suspend fun fetchUsers(): List<User> {
        return service.fetchUsers().map { it.toDomain() }
    }

}