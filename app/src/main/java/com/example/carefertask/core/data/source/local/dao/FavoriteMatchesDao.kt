package com.example.carefertask.core.data.source.local.dao

import androidx.room.*
import com.example.carefertask.core.data.model.dto.MatchDto
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMatchesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(matchDto: MatchDto)

    @Query("SELECT * FROM favorite_matches")
    fun getFavoriteMatches(): Flow<List<MatchDto>>

    @Delete
    suspend fun deleteMatch(matchDto: MatchDto)

}