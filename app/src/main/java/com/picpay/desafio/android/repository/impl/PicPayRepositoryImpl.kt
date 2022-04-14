package com.picpay.desafio.android.repository.impl

import com.picpay.desafio.android.repository.PicPayRepository
import com.picpay.desafio.android.repository.source.LocalUsersSource
import com.picpay.desafio.android.repository.source.RemoteUsersSource
import kotlinx.coroutines.flow.flow
import timber.log.Timber

internal class PicPayRepositoryImpl constructor(
    private val remoteUsersSource: RemoteUsersSource,
    private val localUsersSource: LocalUsersSource
) : PicPayRepository {

    override suspend fun fetchUsers() = flow {
        try {
            val usersRemote = remoteUsersSource.load()
            localUsersSource.saveAll(usersRemote)
        } catch (e: Exception) {
            Timber.e(e)
        }

        val usersLocal = localUsersSource.load()
        emit(usersLocal)
    }
}