package com.picpay.desafio.android.repository

import com.picpay.desafio.android.service.PicPayService

class PicPayRepository constructor(
    private val service: PicPayService
) {

    suspend fun fetchUsers() {
        service.fetchUsers()
    }

}