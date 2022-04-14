package com.picpay.desafio.android.ui.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.picpay.desafio.android.base.BaseViewModel
import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.usecase.FetchUsersUseCase
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class UsersViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when create instance, the state should be empty`() {
        // given
        val sut = UsersViewModelSut(testDispatcher)

        // when
        val viewModel = sut.createViewModel()

        // then
        viewModel.state.value?.users shouldBe emptyList()
        viewModel.state.value?.loading shouldBe false
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when load users, the state should have the returned list`() = runTest {
        // given
        val sut = UsersViewModelSut(testDispatcher)
        val viewModel = sut.createViewModel()
        val user = User(
            id = Int.MAX_VALUE,
            name = "Name of the user",
            username = "Username of the user",
            img = "http://foo.bar"
        )

        sut.users = listOf(user)

        // when
        viewModel.act(UsersAction.LoadUsers)

        advanceUntilIdle()

        // then
        viewModel.state.value?.loading shouldBe false
        viewModel.state.value?.users?.size shouldBe 1
        viewModel.state.value?.users?.first()?.run {
            id shouldBe user.id
            name shouldBe user.name
            img shouldBe user.img
            username shouldBe user.username
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when load users, if there is users stored on SavedState, should return it`() = runTest {
        // given
        val sut = UsersViewModelSut(testDispatcher)
        val userFromUseCase = User(
            id = Int.MAX_VALUE,
            name = "User from use case",
            username = "User from use case",
            img = "http://foo.bar"
        )
        val userFromSavedState = User(
            id = Int.MAX_VALUE,
            name = "User from saved state",
            username = "User from saved state",
            img = "http://foo.bar"
        )
        sut.users = listOf(userFromUseCase)
        sut.addUsersToSavedStateHandle(listOf(userFromSavedState))
        val viewModel = sut.createViewModel()

        // when
        viewModel.act(UsersAction.LoadUsers)

        advanceUntilIdle()

        // then
        viewModel.state.value?.loading shouldBe false
        viewModel.state.value?.users?.size shouldBe 1
        viewModel.state.value?.users?.first()?.run {
            id shouldBe userFromSavedState.id
            name shouldBe userFromSavedState.name
            img shouldBe userFromSavedState.img
            username shouldBe userFromSavedState.username
        }
    }
}

private class UsersViewModelSut(
    private val dispatcher: CoroutineDispatcher
    ) : FetchUsersUseCase {

    private var savedStateHandle: SavedStateHandle? = null

    var users: List<User>? = null

    fun addUsersToSavedStateHandle(users: List<User>) {
        savedStateHandle = SavedStateHandle().apply {
            set(BaseViewModel.SAVED_STATE_KEY, UsersState(users = users))
        }
    }

    fun createViewModel() = UsersViewModel(
        fetchUsersUseCase = this,
        dispatcher = dispatcher,
        savedState = savedStateHandle ?: SavedStateHandle()
    )

    override suspend fun execute(): Flow<List<User>> {
        return users?.run(::flowOf) ?: flowOf(emptyList())
    }
}