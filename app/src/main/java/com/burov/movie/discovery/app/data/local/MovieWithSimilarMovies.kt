package com.burov.movie.discovery.app.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithSimilarMovies(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MovieToSimilar::class,
            parentColumn = "movieId",
            entityColumn = "similarMovieId"
        )
    )
    val similarMovies: List<MovieEntity>
)