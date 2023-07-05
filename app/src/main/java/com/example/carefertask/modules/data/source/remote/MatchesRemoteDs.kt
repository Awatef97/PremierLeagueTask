package com.example.carefertask.modules.data.source.remote

import com.example.carefertask.core.data.model.MatchesResponse
import com.example.carefertask.core.data.source.remote.MatchesService
import javax.inject.Inject

class MatchesRemoteDs @Inject constructor(private val matchesService: MatchesService) {
    suspend fun getMatches(): MatchesResponse {
        return matchesService.getMatches()
    }
}