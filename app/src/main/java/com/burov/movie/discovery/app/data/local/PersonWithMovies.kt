package com.burov.movie.discovery.app.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PersonWithMovies(
    @Embedded val person: PersonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            PersonToMovieEntity::class,
            parentColumn = "personId",
            entityColumn = "movieId"
        )
    )
    val movies: List<MovieEntity>
)