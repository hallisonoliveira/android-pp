package com.picpay.desafio.android.repository

import com.picpay.desafio.android.model.domain.User

interface PicPayRepository {
    suspend fun fetchUsers(): List<User>
}