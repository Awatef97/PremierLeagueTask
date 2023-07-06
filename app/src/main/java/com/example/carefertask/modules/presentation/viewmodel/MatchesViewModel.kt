package com.example.carefertask.modules.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carefertask.R
import com.example.carefertask.modules.domain.entity.MatchEntity
import com.example.carefertask.modules.domain.interactor.ChangeFavoriteStatusUseCase
import com.example.carefertask.modules.domain.interactor.GetMatchesUseCase
import com.example.carefertask.modules.presentation.uimodel.MatchesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val changeFavoriteStatusUseCase: ChangeFavoriteStatusUseCase,
) : ViewModel() {

    private var matchesJob: Job? = null
    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState>
        get() = _uiState

    private val _effect = MutableSharedFlow<Int?>()
    val effect: SharedFlow<Int?>
        get() = _effect

    init {
        collectFiltersState()
    }

    fun changeMatchFavoriteStatus(matchEntity: MatchEntity) {
        viewModelScope.launch {
            changeFavoriteStatusUseCase.invoke(matchEntity)
        }
    }

    fun changeFavoriteSelection(isFav: Boolean) {
        _uiState.value = _uiState.value.copy(isFavoriteSelected = isFav)
    }

    private fun getMatches() {
        matchesJob?.cancel()
        matchesJob = Job()
        viewModelScope.launch(matchesJob!!) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _effect.emit(null)
            try {
                getMatchesUseCase()
                    .collectLatest {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            matchesEntity = it
                        )
                        _effect.emit(null)
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _effect.emit(e.handleError())
            }
        }
    }

    private fun collectFiltersState() {
        viewModelScope.launch {
                getMatches()

        }
    }

    private fun Throwable?.handleError(): Int {
        return if (this is IOException) {
            R.string.internet_error
        } else {
            R.string.something_went_wrong_error
        }
    }

    override fun onCleared() {
        matchesJob?.cancel()
        super.onCleared()
    }
}