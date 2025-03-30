package com.burov.movie.discovery.app.domain.details

import com.burov.movie.discovery.app.data.MovieRepository
import com.burov.movie.discovery.app.domain.UseCase

class FetchSimilarMoviesUseCase(private val movieRepository: MovieRepository) :
    UseCase<String, Result<Unit>> {

    override suspend fun execute(params: String): Result<Unit> {
        return movieRepository.fetchSimilarMovies(params)
    }
}