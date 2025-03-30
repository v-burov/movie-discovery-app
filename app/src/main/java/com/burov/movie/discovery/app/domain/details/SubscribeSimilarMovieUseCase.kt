package com.burov.movie.discovery.app.domain.details

import com.burov.movie.discovery.app.data.MovieRepository
import com.burov.movie.discovery.app.domain.UseCase
import com.burov.movie.discovery.app.domain.movies.MovieItem
import kotlinx.coroutines.flow.Flow

class SubscribeSimilarMovieUseCase(private val movieRepository: MovieRepository) :
    UseCase<String, Flow<List<MovieItem>>> {

    override suspend fun execute(params: String): Flow<List<MovieItem>> {
        return movieRepository.observeSimilarMovies(params)
    }
}