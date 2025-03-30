package com.burov.movie.discovery.app.domain.movies

data class MovieItem(
    val id: String,
    val title: String,
    val posterThumbnailUrl: String?
)