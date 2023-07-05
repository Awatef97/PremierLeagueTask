package com.example.carefertask.modules.data.source.local

import com.example.carefertask.core.data.model.dto.MatchDto
import com.example.carefertask.core.data.source.local.dao.FavoriteMatchesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteMatchesLocalDs @Inject constructor(private val favoriteMatchesDao: FavoriteMatchesDao) {
    fun getFavoriteMatches(): Flow<List<MatchDto>> {
        return favoriteMatchesDao.getFavoriteMatches()
    }

    suspend fun insertMatch(matchDto: MatchDto) {
        favoriteMatchesDao.insertMatch(matchDto)
    }

    suspend fun deleteMatch(matchDto: MatchDto) {
        favoriteMatchesDao.deleteMatch(matchDto)
    }
}