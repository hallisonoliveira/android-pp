package com.picpay.desafio.android.service

import com.picpay.desafio.android.model.dto.UserDto
import retrofit2.http.GET

interface PicPayService {

    @GET("users")
    suspend fun fetchUsers(): List<UserDto>

}