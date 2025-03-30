package com.burov.movie.discovery.app.presentation.details

data class MovieDetails(
    val id: String,
    val title: String,
    val overview: String,
    val posterUrl: String?,
)