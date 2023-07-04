package com.example.carefertask.core.data.source.remote

import com.example.carefertask.BuildConfig
import com.example.carefertask.core.data.model.MatchesResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface MatchesService {
    @GET("competitions/2021/matches")
    suspend fun getMatches(
        @Header("X-Auth-Token") token: String = BuildConfig.API_KEY
    ): MatchesResponse
}