package com.picpay.desafio.android.repository

import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.model.dto.UserDto
import com.picpay.desafio.android.model.mapper.toDomain
import com.picpay.desafio.android.repository.impl.PicPayRepositoryImpl
import com.picpay.desafio.android.repository.source.LocalUsersSource
import com.picpay.desafio.android.repository.source.RemoteUsersSource
import com.picpay.desafio.android.service.PicPayService
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class PicPayRepositoryTest {

    @ExperimentalCoroutinesApi
    @Test
    fun `when fetch users list with one item, should return a list with one item`() = runTest {
        // given
        val sut = PicPayRepositorySut()
        val repository = sut.createRepository()
        val users = listOf(
            User(
                id = Int.MAX_VALUE,
                name = "Name of the user",
                username = "Username of the user",
                img = "http://foo.bar"
            )
        )
        sut.users = users

        // when
        val returnedList = repository.fetchUsers().first()

        // then
        returnedList.size shouldBe 1
        returnedList shouldBe users
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when fetch users list with two items, should return a list with two items`() = runTest {
        // given
        val sut = PicPayRepositorySut()
        val repository = sut.createRepository()
        val users = listOf(
            User(
                id = Int.MAX_VALUE,
                name = "Name of the user",
                username = "Username of the user",
                img = "http://foo.bar"
            ),
            User(
                id = 1234,
                name = "Name of the user 2",
                username = "Username of the user 2",
                img = "http://foo.bar.2"
            )
        )
        sut.users = users

        // when
        val returnedList = repository.fetchUsers().first()

        // then
        returnedList.size shouldBe 2
        returnedList.forEach { returnedUser ->
            users.firstOrNull { it.id == returnedUser.id } shouldNotBe null
        }
    }

    @Test
    fun `when fetch users from remote source, should store them on local source and then return`() = runTest {
        // given
        val sut = PicPayRepositorySut()
        val repository = sut.createRepository()
        val users = listOf(
            User(
                id = Int.MAX_VALUE,
                name = "Name of the user",
                username = "Username of the user",
                img = "http://foo.bar"
            )
        )
        sut.users = users

        // when
        val returnedList = repository.fetchUsers().first()

        // then
        returnedList.size shouldBe 1
        returnedList.forEach { returnedUser ->
            users.firstOrNull { it.id == returnedUser.id } shouldNotBe null
        }
        sut.usersOnLocalSource shouldNotBe null
        sut.usersOnLocalSource!!.forEach { returnedUser ->
            users.firstOrNull { it.id == returnedUser.id } shouldNotBe null
        }
    }
}

/**
 * A class used to help the tests executions.
 *
 * SUT = System Under Test
 */
internal class PicPayRepositorySut : RemoteUsersSource, LocalUsersSource {

    var users: List<User>? = null
    var usersOnLocalSource: List<User>? = null
        private set

    fun createRepository(): PicPayRepository {
        return PicPayRepositoryImpl(
            remoteUsersSource = this,
            localUsersSource = this
        )
    }

    override suspend fun fetch(): List<User> {
        return users ?: emptyList()
    }

    override suspend fun load(): List<User> {
        return usersOnLocalSource ?: emptyList()
    }

    override fun saveAll(users: List<User>) {
        usersOnLocalSource = users
    }
}