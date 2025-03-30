package com.burov.movie.discovery.app.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burov.movie.discovery.app.domain.details.FetchSimilarMoviesUseCase
import com.burov.movie.discovery.app.domain.details.SubscribeMovieDetailsUseCase
import com.burov.movie.discovery.app.domain.details.SubscribeSimilarMovieUseCase
import com.burov.movie.discovery.app.presentation.common.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val fetchSimilarMoviesUseCase: FetchSimilarMoviesUseCase,
    private val subscribeMovieDetailsUseCase: SubscribeMovieDetailsUseCase,
    private val subscribeSimilarMovieUseCase: SubscribeSimilarMovieUseCase,
) : ViewModel() {

    private var _data = MutableStateFlow<MovieDetails?>(null)
    val data = _data.asStateFlow()

    private var _similarMovies = MutableStateFlow<List<MovieItem>>(emptyList())
    val similarMovies = _similarMovies.asStateFlow()

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchSimilarMovies(movieId: String) {
        viewModelScope.launch {
            fetchSimilarMoviesUseCase.execute(params = movieId)
                .onSuccess {
                    _error.update { null }
                }
                .onFailure { exception ->
                    _error.update { exception.message ?: "Something went wrong" }
                }
        }
    }

    fun subscribeToMovieDetails(movieId: String) {
        viewModelScope.launch {
            subscribeMovieDetailsUseCase.execute(params = movieId)
                .collect { movieDetails ->
                    if (movieDetails != null) {
                        _data.update {
                            MovieDetails(
                                id = movieDetails.id,
                                title = movieDetails.title,
                                overview = movieDetails.overview,
                                posterUrl = movieDetails.posterUrl,
                            )
                        }
                    }
                }
        }
    }

    fun subscribeToSimilarMovies(movieId: String) {
        viewModelScope.launch {
            subscribeSimilarMovieUseCase.execute(params = movieId)
                .collect { movies ->
                    _similarMovies.update {
                        movies.map {
                            MovieItem(
                                id = it.id,
                                title = it.title,
                                posterThumbnailUrl = it.posterThumbnailUrl,
                            )
                        }
                    }
                }
        }
    }

}