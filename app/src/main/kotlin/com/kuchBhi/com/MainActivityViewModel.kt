package com.kuchBhi.com

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuchBhi.com.core.domain.usecase.ObserveUserPreferencesUseCase
import com.kuchBhi.com.core.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userPreferences: UserPreferences) : MainActivityUiState
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeUserPreferences: ObserveUserPreferencesUseCase,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = observeUserPreferences()
        .map { prefs -> MainActivityUiState.Success(prefs) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = MainActivityUiState.Loading,
        )
}
