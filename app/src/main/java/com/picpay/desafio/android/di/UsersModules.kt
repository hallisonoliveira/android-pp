package com.picpay.desafio.android.di

import android.content.Context
import androidx.room.Room
import com.picpay.desafio.android.dao.UserDao
import com.picpay.desafio.android.database.AppDatabase
import com.picpay.desafio.android.repository.PicPayRepository
import com.picpay.desafio.android.repository.impl.PicPayRepositoryImpl
import com.picpay.desafio.android.repository.source.*
import com.picpay.desafio.android.repository.source.LocalUsersSourceImpl
import com.picpay.desafio.android.service.PicPayService
import com.picpay.desafio.android.usecase.FetchUsersUseCase
import com.picpay.desafio.android.usecase.impl.FetchUsersUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object UsersModules {

    @Provides
    fun providesUserDao(
        database: AppDatabase
    ): UserDao {
        return database.userDao()
    }

    @Provides
    fun providesDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "PicPayDatabase"
        ).build()
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideFetchUsersUseCase(
        repository: PicPayRepository
    ) : FetchUsersUseCase {
        return FetchUsersUseCaseImpl(repository)
    }

    @Provides
    fun providePicPayRepository(
        remoteUsersSource: RemoteUsersSource,
        localUsersSource: LocalUsersSource
    ): PicPayRepository {
        return PicPayRepositoryImpl(
            remoteUsersSource = remoteUsersSource,
            localUsersSource = localUsersSource
        )
    }

    @Provides
    fun provideRemoteUsersSource(service: PicPayService): RemoteUsersSource {
        return RemoteUsersSourceImpl(service)
    }

    @Provides
    fun provideLocalUsersSource(dao: UserDao): LocalUsersSource {
        return LocalUsersSourceImpl(dao)
    }
}