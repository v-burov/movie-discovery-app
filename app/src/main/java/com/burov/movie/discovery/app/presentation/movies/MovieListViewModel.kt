package com.burov.movie.discovery.app.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burov.movie.discovery.app.domain.movies.FetchMoviesUseCase
import com.burov.movie.discovery.app.domain.movies.SubscribeMoviesUseCase
import com.burov.movie.discovery.app.presentation.common.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val fetchMoviesUseCase: FetchMoviesUseCase,
    private val subscribeMoviesUseCase: SubscribeMoviesUseCase,
) : ViewModel() {

    private var _data = MutableStateFlow<List<MovieItem>>(emptyList())
    val data = _data.asStateFlow()

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchMovies(personId: String) {
        viewModelScope.launch {
            fetchMoviesUseCase.execute(params = personId)
                .onSuccess {
                    _error.update { null }
                }
                .onFailure { exception ->
                    _error.update { exception.message ?: "Something went wrong" }
                }
        }
    }

    fun subscribeToMovies(personId: String) {
        viewModelScope.launch {
            subscribeMoviesUseCase.execute(params = personId)
                .collect { movies ->
                    _data.update {
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