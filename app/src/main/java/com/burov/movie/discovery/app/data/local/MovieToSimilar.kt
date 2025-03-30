package com.burov.movie.discovery.app.data.local

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "similarMovieId"])
data class MovieToSimilar(
    val movieId: String,
    val similarMovieId: String
)