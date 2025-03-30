package com.burov.movie.discovery.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String,
    val title: String,
    val overview: String,
    val posterThumbnailUrl: String?,
    val posterUrl: String?,
)