package com.example.carefertask.modules.domain.interactor

import com.example.carefertask.modules.domain.entity.MatchEntity
import com.example.carefertask.modules.domain.repository.MatchesRepository
import javax.inject.Inject

class ChangeFavoriteStatusUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository
    ) {
    suspend operator fun invoke(matchEntity: MatchEntity) {
        return if (matchEntity.isFavorite) {
            matchesRepository.removeFromFavorite(matchEntity)
        } else {
            matchesRepository.addToFavorite(matchEntity)
        }
    }
}