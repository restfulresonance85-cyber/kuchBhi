package com.kuchBhi.com.core.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Marker interface for immutable screen state in MVI.
 */
interface UiState

/**
 * Marker interface for user actions / intents dispatched from the UI to the ViewModel.
 */
interface UiIntent

/**
 * Marker interface for one-off side effects (navigation, snackbars, toasts).
 */
interface UiEffect

/**
 * Enterprise MVI base ViewModel enforcing Unidirectional Data Flow (UDF):
 *  - UI dispatches [UiIntent] via [onIntent]
 *  - ViewModel reduces state atomically via [updateState] and exposes immutable [uiState]
 *  - One-shot side effects are delivered via a buffered [Channel] exposed as [effects]
 */
abstract class BaseMviViewModel<S : UiState, I : UiIntent, E : UiEffect>(
    initialState: S,
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = _effects.receiveAsFlow()

    protected val currentState: S
        get() = _uiState.value

    abstract fun onIntent(intent: I)

    protected fun updateState(reducer: S.() -> S) {
        _uiState.update(reducer)
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
