package com.burov.movie.discovery.app.data

import com.burov.movie.discovery.app.domain.details.MovieDetails
import com.burov.movie.discovery.app.domain.movies.MovieItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun fetchMovies(personId: String): Result<Unit>

    suspend fun fetchSimilarMovies(movieId: String): Result<Unit>

    fun observeMovies(personId: String): Flow<List<MovieItem>>

    fun observeSimilarMovies(movieId: String): Flow<List<MovieItem>>

    fun observeMovieDetails(movieId: String): Flow<MovieDetails?>
}