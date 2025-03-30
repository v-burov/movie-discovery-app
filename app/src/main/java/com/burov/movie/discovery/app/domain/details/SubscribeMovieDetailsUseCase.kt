package com.burov.movie.discovery.app.domain.details

import com.burov.movie.discovery.app.data.MovieRepository
import com.burov.movie.discovery.app.domain.UseCase
import kotlinx.coroutines.flow.Flow

class SubscribeMovieDetailsUseCase(private val movieRepository: MovieRepository) :
    UseCase<String, Flow<MovieDetails?>> {

    override suspend fun execute(params: String): Flow<MovieDetails?> {
        return movieRepository.observeMovieDetails(params)
    }
}