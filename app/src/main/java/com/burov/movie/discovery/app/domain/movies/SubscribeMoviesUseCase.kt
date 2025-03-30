package com.burov.movie.discovery.app.domain.movies

import com.burov.movie.discovery.app.data.MovieRepository
import com.burov.movie.discovery.app.domain.UseCase
import kotlinx.coroutines.flow.Flow

class SubscribeMoviesUseCase(private val movieRepository: MovieRepository) :
    UseCase<String, Flow<List<MovieItem>>> {

    override suspend fun execute(params: String): Flow<List<MovieItem>> {
        return movieRepository.observeMovies(params)
    }
}