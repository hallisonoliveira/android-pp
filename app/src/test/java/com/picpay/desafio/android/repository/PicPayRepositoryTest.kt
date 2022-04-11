package com.picpay.desafio.android.repository

import com.nhaarman.mockitokotlin2.mock
import com.picpay.desafio.android.model.dto.UserDto
import com.picpay.desafio.android.model.mapper.toDomain
import com.picpay.desafio.android.service.PicPayService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Call

@RunWith(JUnit4::class)
internal class PicPayRepositoryTest {

    @Test
    fun `when create instance, should have not any request`() {
        // given
        val sut = PicPayRepositorySut()

        // when
        sut.createRepository()

        // then
        assert(sut.amountRequests == 0)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when fetch users once, should have only one request`() = runTest {
        //given
        val sut = PicPayRepositorySut()
        val repository = sut.createRepository()

        // when
        repository.fetchUsers()

        // then
        assert(sut.amountRequests == 1)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when fetch users twice, should have only two requests`() = runTest {
        //given
        val sut = PicPayRepositorySut()
        val repository = sut.createRepository()

        // when
        repository.fetchUsers()
        repository.fetchUsers()

        // then
        assert(sut.amountRequests == 2)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when fetch users list with one item, should return a list with one item`() = runTest {
        // given
        val sut = PicPayRepositorySut()
        val repository = sut.createRepository()
        val usersDto = listOf(
            UserDto(
                id = Int.MAX_VALUE,
                name = "Name of the user",
                username = "Username of the user",
                img = "http://foo.bar"
            )
        )
        sut.users = usersDto
        val usersDomain = usersDto.map { it.toDomain() }

        // when
        val returnedList = repository.fetchUsers()

        // then
        assert(returnedList.size == 1)
        assert(returnedList == usersDomain)
    }

}

/**
 * A class used to help the tests executions.
 *
 * SUT = System Under Test
 */
private class PicPayRepositorySut : PicPayService {

    var amountRequests: Int = 0
       private set

    var users: List<UserDto>? = null

    fun createRepository(): PicPayRepository {
        return PicPayRepository(this)
    }

    override fun getUsers(): Call<List<com.picpay.desafio.android.User>> {
        return mock()
    }

    override suspend fun fetchUsers(): List<UserDto> {
        amountRequests++
        return users ?: emptyList()
    }

}