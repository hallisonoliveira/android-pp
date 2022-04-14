package com.picpay.desafio.android.ui.users

import android.os.Parcelable
import androidx.lifecycle.*
import com.picpay.desafio.android.base.Action
import com.picpay.desafio.android.base.BaseViewModel
import com.picpay.desafio.android.base.Command
import com.picpay.desafio.android.base.State
import com.picpay.desafio.android.model.domain.User
import com.picpay.desafio.android.usecase.FetchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val dispatcher: CoroutineDispatcher,
    savedState: SavedStateHandle
) : BaseViewModel<UsersState, UsersCommand, UsersAction>(
    savedState,
    UsersState()
) {

    fun act(action: UsersAction) {
        when (action) {
            is UsersAction.LoadUsers -> viewModelScope.launch(dispatcher) { loadUsers() }
        }
    }

    private suspend fun loadUsers() {
        newState(currentState.copy(loading = true))

        val currentUsersList = currentState.users

        if (currentUsersList.isNotEmpty())
            newState(currentState.copy(users = currentUsersList, loading = false))

        fetchUsersUseCase.execute().collect { users ->
            newState(currentState.copy(users = users, loading = false))
        }
    }
}

object UsersCommand : Command

@Parcelize
data class UsersState(
    val loading: Boolean = false,
    val users: List<User> = emptyList()
) : State, Parcelable

sealed class UsersAction : Action {
    object LoadUsers : UsersAction()
}