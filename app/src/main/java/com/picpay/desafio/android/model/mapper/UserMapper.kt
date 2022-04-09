package com.picpay.desafio.android.model.mapper

import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.model.dto.UserDto

fun UserDto.toDomain() = User(
    id = id,
    name = name,
    username = username,
    img = img
)