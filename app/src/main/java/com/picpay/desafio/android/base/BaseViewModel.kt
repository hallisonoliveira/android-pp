package com.picpay.desafio.android.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<STATE: State, COMMAND: Command, ACTION: Action>(
    private val savedState: SavedStateHandle,
    initialState: STATE
) : ViewModel() {

    private val _state: MutableLiveData<STATE> by lazy {
        MutableLiveData(savedState.get(SAVED_STATE_KEY) ?: initialState)
    }
    val state: LiveData<STATE> get() = _state

    private val _command = Channel<COMMAND> (Channel.RENDEZVOUS)
    val command = _command.receiveAsFlow()
    
    val currentState = _state.value!!

    fun newState(state: STATE) {
        savedState[SAVED_STATE_KEY] = state
        _state.value = state
    }
    
    companion object {
        private const val SAVED_STATE_KEY = "state"
    }

}