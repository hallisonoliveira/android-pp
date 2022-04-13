package com.picpay.desafio.android.di

import com.picpay.desafio.android.repository.PicPayRepository
import com.picpay.desafio.android.usecase.FetchUsersUseCase
import com.picpay.desafio.android.usecase.impl.FetchUsersUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class UsersModules {

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideFetchUsersUseCase(
        repository: PicPayRepository
    ): FetchUsersUseCase = FetchUsersUseCaseImpl(repository)
}