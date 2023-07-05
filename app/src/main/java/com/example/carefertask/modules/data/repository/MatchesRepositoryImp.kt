package com.example.carefertask.modules.data.repository

import com.example.carefertask.core.data.model.MatchesItem
import com.example.carefertask.modules.data.mapper.toDto
import com.example.carefertask.modules.data.mapper.toEntity
import com.example.carefertask.modules.data.source.local.FavoriteMatchesLocalDs
import com.example.carefertask.modules.data.source.remote.MatchesRemoteDs
import com.example.carefertask.modules.domain.entity.MatchEntity
import com.example.carefertask.modules.domain.entity.MatchesEntity
import com.example.carefertask.modules.domain.repository.MatchesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchesRepositoryImp @Inject constructor(
    private val matchesRemoteDs: MatchesRemoteDs,
    private val favoriteMatchesLocalDs: FavoriteMatchesLocalDs,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): MatchesRepository {
    override suspend fun getMatches(): Flow<MatchesEntity> {
        val remoteMatches = withContext(ioDispatcher) {
            matchesRemoteDs.getMatches()
        }
        return favoriteMatchesLocalDs.getFavoriteMatches().distinctUntilChanged()
            .map { localMatches ->
                val matchesEntity: List<MatchEntity?>?
                val localIds = localMatches.map { it.id }
                if (localMatches.isNotEmpty()) {
                    val remoteFavoriteMatches: List<MatchesItem?>? =
                        remoteMatches.matches?.filter {
                            it?.id in localIds
                        }
                    remoteFavoriteMatches?.map {
                        it?.let {
                            favoriteMatchesLocalDs.insertMatch(it.toEntity(true).toDto())
                        }
                    }
                    matchesEntity = remoteMatches.matches?.map {
                        it?.toEntity(remoteFavoriteMatches?.contains(it) == true)
                    }
                } else {
                    matchesEntity = remoteMatches.matches?.map {
                        it?.toEntity(false)
                    }
                }
                MatchesEntity(matchesEntity)
            }.flowOn(ioDispatcher)
    }

    override suspend fun addToFavorite(matchEntity: MatchEntity) {
        favoriteMatchesLocalDs.insertMatch(matchEntity.toDto())
    }

    override suspend fun removeFromFavorite(matchEntity: MatchEntity) {
        favoriteMatchesLocalDs.deleteMatch(matchEntity.toDto())
    }
}