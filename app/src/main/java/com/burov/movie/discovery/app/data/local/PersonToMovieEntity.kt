package com.burov.movie.discovery.app.data.local

import androidx.room.Entity

@Entity(primaryKeys = ["personId", "movieId"])
data class PersonToMovieEntity(
    val personId: String,
    val movieId: String,
)