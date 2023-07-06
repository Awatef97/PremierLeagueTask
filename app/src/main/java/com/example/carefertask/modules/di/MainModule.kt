package com.example.carefertask.modules.di

import com.example.carefertask.core.data.source.local.dao.FavoriteMatchesDao
import com.example.carefertask.core.data.source.remote.MatchesService
import com.example.carefertask.modules.data.repository.MatchesRepositoryImp
import com.example.carefertask.modules.data.source.local.FavoriteMatchesLocalDs
import com.example.carefertask.modules.data.source.remote.MatchesRemoteDs
import com.example.carefertask.modules.domain.repository.MatchesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
object MainModule {
    @ViewModelScoped
    @Provides
    fun provideMainRepositoryImpl(
        matchesRemoteDs: MatchesRemoteDs,
        favoriteMatchesLocalDs: FavoriteMatchesLocalDs,
        dispatcher: CoroutineDispatcher
    ): MatchesRepository {
        return MatchesRepositoryImp(matchesRemoteDs, favoriteMatchesLocalDs, dispatcher)
    }

    @ViewModelScoped
    @Provides
    fun provideFavoriteMatchesLocalDs(favoriteMatchesDao: FavoriteMatchesDao): FavoriteMatchesLocalDs {
        return FavoriteMatchesLocalDs(favoriteMatchesDao)
    }

    @ViewModelScoped
    @Provides
    fun provideFavoriteMatchesRemoteDs(matchesService: MatchesService): MatchesRemoteDs {
        return MatchesRemoteDs(matchesService)
    }
}