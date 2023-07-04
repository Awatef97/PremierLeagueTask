package com.example.carefertask.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.carefertask.core.data.model.dto.MatchDto
import com.example.carefertask.core.data.source.local.dao.FavoriteMatchesDao

@Database(entities = [MatchDto::class], version = 1)
abstract class PremierLeagueDatabase : RoomDatabase() {
    abstract fun favoriteMatchesDao(): FavoriteMatchesDao
}

const val FAVORITE_MATCHES_TABLE = "favorite_matches"
