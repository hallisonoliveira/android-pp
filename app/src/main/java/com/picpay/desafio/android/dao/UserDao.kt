package com.picpay.desafio.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.picpay.desafio.android.model.local.UserLocal
import androidx.room.OnConflictStrategy

@Dao
interface UserDao {

    @Query("SELECT * FROM userLocal")
    fun getAll(): List<UserLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserLocal>)

}