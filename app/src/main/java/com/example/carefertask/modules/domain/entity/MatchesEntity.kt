package com.example.carefertask.modules.domain.entity

data class MatchesEntity(
    val matches: List<MatchEntity?>?,
    val favoriteMatches: List<MatchEntity?>? = null,
    val pinnedMatches: List<MatchEntity?>? = null,
    val pinnedFavoritesMatches: List<MatchEntity?>? = null
)

data class MatchEntity(
    val id: Long?,
    val date: String?,
    val status: String?,
    val score: Score?,
    val competitors: Competitors?,
    val isFavorite: Boolean,
    val isDate: Boolean = false
)

data class Score(
    val home: Int?,
    val away: Int?
)

data class Competitors(
    val home: String?,
    val away: String?
)