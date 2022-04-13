package com.picpay.desafio.android.service

import com.google.gson.stream.MalformedJsonException
import com.picpay.desafio.android.model.dto.UserDto
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
internal class PicPayServiceTest {

    private lateinit var mockWebServer: MockWebServer

    private lateinit var service: PicPayService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("foo/bar/").toString())
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PicPayService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when service returns a request error, should throw an exception`() {
        // given
        val emptyBody = ""
        setupWebserverReturn(HttpURLConnection.HTTP_BAD_REQUEST, emptyBody)

        // when
        val exception = assertThrows<Exception> {
            runTest { service.fetchUsers() }
        }

        // then
        exception.shouldBeInstanceOf<HttpException>()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when service returns an invalid content, should throw an exception`() {
        // given
        val invalidJson = "invalidJsonContent"
        setupWebserverReturn(HttpURLConnection.HTTP_OK, invalidJson)

        // when
        val exception = assertThrows<Exception> {
            runTest { service.fetchUsers() }
        }

        // then
        exception.shouldBeInstanceOf<MalformedJsonException>()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when service returns a valid json with a single object, should throw an exception`() {
        // given
        val user = """
            {
                "id":"1",
                "name":"Foo Bar",
                "img":"https://foo/bar.jpg",
                "username":"foo_bar"
            }
        """
        setupWebserverReturn(HttpURLConnection.HTTP_OK, user)

        // when
        val exception = assertThrows<Exception> {
            runTest { service.fetchUsers() }
        }

        // then
        exception.shouldBeInstanceOf<IllegalStateException>()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when service returns an empty list, should return an empty list`() = runTest {
        // given
        val emptyList = "[]"
        setupWebserverReturn(HttpURLConnection.HTTP_OK, emptyList)

        // when
        val returnedList = service.fetchUsers()

        // then
        returnedList shouldBe emptyList()
        returnedList.shouldBeInstanceOf<List<UserDto>>()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when service returns a list with one item, should return a list with one item`() = runTest {
        // given
        val listWithSingleItem = """
            [
                {
                    "id":"1",
                    "name":"Foo Bar",
                    "img":"https://foo/bar.jpg",
                    "username":"foo_bar"
                }
            ]
        """
        setupWebserverReturn(HttpURLConnection.HTTP_OK, listWithSingleItem)

        // when
        val returnedList = service.fetchUsers()

        // then
        returnedList.size shouldBe 1
        returnedList.shouldBeInstanceOf<List<UserDto>>()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when service returns a list with two items, should return a list with two items`() = runTest {
        // given
        val listWithSingleItem = """
            [
                {
                    "id":"1",
                    "name":"Foo Bar",
                    "img":"https://foo/bar.jpg",
                    "username":"foo_bar"
                },
                {
                    "id":"2",
                    "name":"Foo Bar 2",
                    "img":"https://foo/bar_2.jpg",
                    "username":"foo_bar_2"
                }
            ]
        """
        setupWebserverReturn(HttpURLConnection.HTTP_OK, listWithSingleItem)

        // when
        val returnedList = service.fetchUsers()

        // then
        returnedList.size shouldBe 2
        returnedList.shouldBeInstanceOf<List<UserDto>>()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when service returns a list with a valid item, should return a lista with the right content`() = runTest {
        // given
        val userDto = UserDto(
            id = 1,
            name = "Foo Bar",
            img = "https://foo/bar.jpg",
            username = "foo_bar"
        )

        val listWithSingleItem = """
            [
                {
                    "id":"${userDto.id}",
                    "name":"${userDto.name}",
                    "img":"${userDto.img}",
                    "username":"${userDto.username}"
                }
            ]
        """
        setupWebserverReturn(HttpURLConnection.HTTP_OK, listWithSingleItem)

        // when
        val returnedList = service.fetchUsers()

        // then
        returnedList.size shouldBe 1
        returnedList.first().run {
            id shouldBe userDto.id
            name shouldBe userDto.name
            img shouldBe userDto.img
            username shouldBe userDto.username
        }
        returnedList.shouldBeInstanceOf<List<UserDto>>()
    }

    private fun setupWebserverReturn(code: Int, body: String) {
        mockWebServer.enqueue(
            MockResponse().apply {
                setResponseCode(code)
                setBody(body)
            }
        )
    }
}