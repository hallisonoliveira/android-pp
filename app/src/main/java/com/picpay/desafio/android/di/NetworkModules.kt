package com.picpay.desafio.android.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.constants.NetworkConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModules {

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun providesOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .build()
    }

    @Provides
    fun provideRetrofit(
        okHttp: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}