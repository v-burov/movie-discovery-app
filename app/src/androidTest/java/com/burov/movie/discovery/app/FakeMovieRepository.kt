package com.burov.movie.discovery.app

import com.burov.movie.discovery.app.data.MovieRepository
import com.burov.movie.discovery.app.domain.details.MovieDetails
import com.burov.movie.discovery.app.domain.movies.MovieItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeMovieRepository : MovieRepository {

    val moviesFlow = MutableStateFlow<List<MovieItem>>(emptyList())

    val errorFlow = MutableStateFlow<Throwable?>(null)

    private val movieDetails = MovieDetails("movie1", "Doctor Strange", "Description", "url1")

    override suspend fun fetchMovies(personId: String): Result<Unit> {
        return if (errorFlow.value != null) {
            Result.failure(errorFlow.value ?: Exception())
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun fetchSimilarMovies(movieId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override fun observeMovies(personId: String): Flow<List<MovieItem>> {
        return moviesFlow
    }

    override fun observeSimilarMovies(movieId: String): Flow<List<MovieItem>> {
        return flow { emit(moviesFlow.value) }
    }

    override fun observeMovieDetails(movieId: String): Flow<MovieDetails?> {
        return flow { emit(movieDetails.takeIf { it.id == movieId }) }
    }
}