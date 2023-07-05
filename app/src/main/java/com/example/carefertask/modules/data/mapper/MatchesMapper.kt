package com.example.carefertask.modules.data.mapper

import com.example.carefertask.core.data.model.MatchesItem
import com.example.carefertask.core.data.model.dto.MatchDto
import com.example.carefertask.core.extension.getFormattedDate
import com.example.carefertask.modules.domain.entity.Competitors
import com.example.carefertask.modules.domain.entity.MatchEntity
import com.example.carefertask.modules.domain.entity.Score

fun MatchesItem.toEntity(isFavorite: Boolean) =
    MatchEntity(
        id = id,
        date = utcDate?.getFormattedDate(),
        status = status,
        score = Score(
            home = score?.fullTime?.home,
            away = score?.fullTime?.away
        ),
        competitors = Competitors(
            home = homeTeam?.name,
            away = awayTeam?.name
        ),
        isFavorite = isFavorite
    )

fun List<MatchDto>.toEntity() =
    this.map {
        MatchEntity(
            id = it.id,
            date = it.date,
            status = it.status,
            score = Score(home = it.homeScore, away = it.awayScore),
            competitors = Competitors(home = it.homeName, away = it.awayName),
            isFavorite = true
        )
    }

fun MatchEntity.toDto() = MatchDto(
    id = id ?: 0, date = date, status = status, homeScore = score?.home,
    awayScore = score?.away,
    homeName = competitors?.home,
    awayName = competitors?.away
)