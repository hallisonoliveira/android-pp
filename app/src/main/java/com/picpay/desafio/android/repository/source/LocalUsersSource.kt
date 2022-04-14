package com.picpay.desafio.android.repository.source

import com.picpay.desafio.android.dao.UserDao
import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.model.mapper.toDomain
import com.picpay.desafio.android.model.mapper.toLocal

interface LocalUsersSource {
    suspend fun load(): List<User>
    fun saveAll(users: List<User>)
}

internal class LocalUsersSourceImpl constructor(
    private val dao: UserDao
) : LocalUsersSource {

    override suspend fun load(): List<User> {
        return dao.getAll().map { it.toDomain() }
    }

    override fun saveAll(users: List<User>) {
        dao.insertAll(users.map { it.toLocal() })
    }
}