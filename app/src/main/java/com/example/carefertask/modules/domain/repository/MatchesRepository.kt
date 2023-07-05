package com.example.carefertask.modules.domain.repository

import com.example.carefertask.modules.domain.entity.MatchEntity
import com.example.carefertask.modules.domain.entity.MatchesEntity
import kotlinx.coroutines.flow.Flow

interface MatchesRepository {
    suspend fun getMatches(): Flow<MatchesEntity>
    suspend fun addToFavorite(matchEntity: MatchEntity)
    suspend fun removeFromFavorite(matchEntity: MatchEntity)
}