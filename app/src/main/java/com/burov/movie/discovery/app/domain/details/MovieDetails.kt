package com.burov.movie.discovery.app.domain.details

data class MovieDetails(
    val id: String,
    val title: String,
    val overview: String,
    val posterUrl: String?,
)