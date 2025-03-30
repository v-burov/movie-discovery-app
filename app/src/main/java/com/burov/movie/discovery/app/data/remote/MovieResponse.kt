package com.burov.movie.discovery.app.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("results") val movies: List<MovieResult>,
)

@Serializable
data class MovieResult(
    val id: String,
    val title: String,
    val overview: String,
    @SerialName("backdrop_path") val posterThumbnailPath: String?,
    @SerialName("poster_path") val posterPath: String?,
)