package com.picpay.desafio.android.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserLocal(
    @PrimaryKey
    val id: Int,
    val name: String,
    val username: String,
    val img: String
)