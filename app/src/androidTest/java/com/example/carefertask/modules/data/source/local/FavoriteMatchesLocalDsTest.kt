package com.example.carefertask.modules.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.carefertask.core.data.model.dto.MatchDto
import com.example.carefertask.core.data.source.local.PremierLeagueDatabase
import com.example.carefertask.core.data.source.local.dao.FavoriteMatchesDao
import io.mockk.MockKAnnotations
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavoriteMatchesLocalDsTest {
    private lateinit var db: PremierLeagueDatabase
    private lateinit var favoriteMatchesDao: FavoriteMatchesDao

    @Before
    fun createDb() {
        MockKAnnotations.init(this)
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, PremierLeagueDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        favoriteMatchesDao = db.favoriteMatchesDao()
    }

    @Test
    fun insertMatch_returnMatch(): Unit = runBlocking {
        // given
        val matchDto = MatchDto(
            123456,
            "May 28 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Morocco"
        )
        // when
        favoriteMatchesDao.insertMatch(matchDto)
        // then
        val matches: List<MatchDto> =
            favoriteMatchesDao.getFavoriteMatches().first()
        assertEquals(matchDto, matches[0])
    }

    @Test
    fun removeMatch_returnNothing(): Unit = runBlocking {
        // given
        val matchDto = MatchDto(
            123456,
            "Aug 16 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Morocco"
        )
        favoriteMatchesDao.insertMatch(matchDto)
        // when
        favoriteMatchesDao.deleteMatch(matchDto)
        // then
        val matches: List<MatchDto> =
            favoriteMatchesDao.getFavoriteMatches().first()
        TestCase.assertTrue(matches.isEmpty())
    }

    @Test
    fun getMatches_returnMatchesList(): Unit = runBlocking {
        // given
        val matchDto1 = MatchDto(
            123456,
            "Aug 16 Mon",
            "FINISHED",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Morocco"
        )
        val matchDto2 = MatchDto(
            654321,
            "Aug 16 Mon",
            "IN_PLAY",
            homeScore = 2,
            awayScore = 1,
            homeName = "Egypt",
            awayName = "Morocco"
        )
        favoriteMatchesDao.insertMatch(matchDto1)
        favoriteMatchesDao.insertMatch(matchDto2)
        // when
        favoriteMatchesDao.getFavoriteMatches()
        // then
        val matches: List<MatchDto> =
            favoriteMatchesDao.getFavoriteMatches().first()
        assertEquals(matchDto1, matches[0])
    }

    @After
    fun closeDb() {
        db.close()
    }
}