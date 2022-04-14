package com.picpay.desafio.android.model.mapper

import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.model.dto.UserDto
import com.picpay.desafio.android.model.local.UserLocal

fun UserDto.toDomain() = User(
    id = id,
    name = name,
    username = username,
    img = img
)

fun UserLocal.toDomain() = User(
    id = id,
    name = name,
    username = username,
    img = img
)

fun User.toLocal() = UserLocal(
    id = id,
    name = name,
    username = username,
    img = img
)