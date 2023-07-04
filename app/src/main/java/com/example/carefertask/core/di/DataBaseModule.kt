package com.example.carefertask.core.di

import android.content.Context
import androidx.room.Room
import com.example.carefertask.core.data.source.local.PremierLeagueDatabase
import com.example.carefertask.core.data.source.local.dao.FavoriteMatchesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Volatile
    private var instance: PremierLeagueDatabase? = null
    private const val DATABASE_NAME = "premier_league_db"

    @Singleton
    @Provides
    fun provideDatabaseInstance(@ApplicationContext context: Context): PremierLeagueDatabase {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }

    private fun buildDatabase(context: Context): PremierLeagueDatabase {
        return Room.databaseBuilder(context, PremierLeagueDatabase::class.java, DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideFavoriteMatchesDao(db: PremierLeagueDatabase): FavoriteMatchesDao {
        return db.favoriteMatchesDao()
    }
}