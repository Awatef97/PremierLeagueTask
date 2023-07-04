package com.example.carefertask.core.data.model

data class MatchesItem(
    val awayTeam: AwayTeam? = null,
    val homeTeam: HomeTeam? = null,
    val id: Int? = null,
    val score: Score? = null,
    val status: String? = null,
    val utcDate: String? = null,
)