package com.example.carefertask.modules.presentation.uimodel

import com.example.carefertask.modules.domain.entity.MatchesEntity


data class MatchesUiState(
    val isLoading: Boolean = false,
    val isFavoriteSelected: Boolean = false,
    val matchesEntity: MatchesEntity = MatchesEntity(listOf())
)
