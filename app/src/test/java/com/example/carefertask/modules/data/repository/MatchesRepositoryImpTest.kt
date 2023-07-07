package com.example.carefertask.modules.data.repository

import com.example.carefertask.core.data.model.*
import com.example.carefertask.core.data.model.dto.MatchDto
import com.example.carefertask.modules.data.source.local.FavoriteMatchesLocalDs
import com.example.carefertask.modules.data.source.remote.MatchesRemoteDs
import com.example.carefertask.modules.domain.entity.Competitors
import com.example.carefertask.modules.domain.entity.MatchEntity
import com.example.carefertask.modules.domain.entity.MatchesEntity
import com.example.carefertask.modules.domain.entity.Score
import com.example.carefertask.modules.domain.repository.MatchesRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MatchesRepositoryImpTest{
    private lateinit var SUT: MatchesRepository

    @MockK
    lateinit var matchesRemoteDs: MatchesRemoteDs

    @RelaxedMockK
    lateinit var favoritesLocalDs: FavoriteMatchesLocalDs

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        SUT = MatchesRepositoryImp(matchesRemoteDs, favoritesLocalDs, testDispatcher)
    }

    @Test
    fun getMatches() = runTest(testDispatcher) {
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
        val matchEntity = MatchEntity(
            123456,
            "Fri 5 Aug",
            "FINISHED",
            score = Score(home = 2, away = 1),
            competitors = Competitors(home = "Egypt", "Morocco"),
            isFavorite = true,
            isDate = false
        )
        val mainResponse = MatchesResponse(
            matches = arrayListOf(
                MatchesItem(
                    id = 123456,
                    utcDate = "2022-08-05T19:00:00Z",
                    status = "FINISHED",
                    homeTeam = HomeTeam(name = "Egypt"),
                    awayTeam = AwayTeam(name = "Morocco"),
                    score = Score(fullTime = FullTime(home = 2, away = 1))
                )
            )
        )
        // when
        coEvery { favoritesLocalDs.insertMatch(matchDto) }.just(Runs)
        coEvery { favoritesLocalDs.getFavoriteMatches() }.returns(flow {
            emit(
                arrayListOf(matchDto)
            )
        })
        coEvery { matchesRemoteDs.getMatches() }.returns(mainResponse)
        // then
        val matchesEntity: MatchesEntity = SUT.getMatches().first()
        assertEquals(matchesEntity.matches?.get(0), matchEntity)
    }

    @Test
    fun addToFavorites() = runTest(testDispatcher) {
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
        val matchEntity = MatchEntity(
            123456,
            "Fri 5 Aug",
            "FINISHED",
            score = Score(home = 2, away = 1),
            competitors = Competitors(home = "Egypt", "Morocco"),
            isFavorite = true,
            isDate = false
        )
        val mainResponse = MatchesResponse(
            matches = arrayListOf(
                MatchesItem(
                    id = 123456,
                    utcDate = "2022-08-05T19:00:00Z",
                    status = "FINISHED",
                    homeTeam = HomeTeam(name = "Egypt"),
                    awayTeam = AwayTeam(name = "Morocco"),
                    score = Score(fullTime = FullTime(home = 2, away = 1))
                )
            )
        )
        // when
        SUT.addToFavorite(matchEntity)
        coEvery { favoritesLocalDs.getFavoriteMatches() }.returns(flow {
            emit(
                arrayListOf(matchDto)
            )
        })
        coEvery { matchesRemoteDs.getMatches() }.returns(mainResponse)
        // then
        val entity: MatchEntity? = SUT.getMatches().first().matches?.get(0)
        assertEquals(entity, matchEntity)
    }

    @Test
    fun removeFromFavorites() = runTest(testDispatcher) {
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
        val matchEntity = MatchEntity(
            123456,
            "Fri 5 Aug",
            "FINISHED",
            score = Score(home = 2, away = 1),
            competitors = Competitors(home = "Egypt", "Morocco"),
            isFavorite = true,
            isDate = false
        )
        val mainResponse = MatchesResponse(
            matches = arrayListOf(
                MatchesItem(
                    id = 123456,
                    utcDate = "2022-08-05T19:00:00Z",
                    status = "FINISHED",
                    homeTeam = HomeTeam(name = "Egypt"),
                    awayTeam = AwayTeam(name = "Morocco"),
                    score = Score(fullTime = FullTime(home = 2, away = 1))
                )
            )
        )
        favoritesLocalDs.insertMatch(matchDto)
        // when
        SUT.removeFromFavorite(matchEntity)
        coEvery { favoritesLocalDs.getFavoriteMatches() }.returns(flow {
            emit(
                arrayListOf()
            )
        })
        coEvery { matchesRemoteDs.getMatches() }.returns(mainResponse)
        // then
        val entity: MatchEntity? = SUT.getMatches().first().matches?.get(0)
        TestCase.assertEquals(entity?.isFavorite, false)
    }
}