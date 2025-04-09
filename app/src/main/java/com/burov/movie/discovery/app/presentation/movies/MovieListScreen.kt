package com.burov.movie.discovery.app.presentation.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.burov.movie.discovery.app.presentation.common.MovieItem
import com.burov.movie.discovery.app.presentation.common.MovieList
import org.koin.compose.viewmodel.koinViewModel

private const val BENEDICT_CUMBERBATCH_ID = "71580"

@Composable
fun MovieListScreen(
    onMovieSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = koinViewModel(),
) {
    val movies by viewModel.data.collectAsState()
    val error by viewModel.error.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.run {
            viewModel.subscribeToMovies(personId = BENEDICT_CUMBERBATCH_ID)
            viewModel.fetchMovies(personId = BENEDICT_CUMBERBATCH_ID)
        }
    }

    LaunchedEffect(error) {
        error?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        contentColor = MaterialTheme.colorScheme.onError,
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
        content = { contentPadding ->
            MovieListScreenContent(
                modifier = modifier.padding(contentPadding),
                movies = movies,
            ) {
                onMovieSelected.invoke(it)
            }
        }
    )
}

@Composable
private fun MovieListScreenContent(
    modifier: Modifier,
    movies: List<MovieItem>,
    onMovieSelected: (String) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (movies.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                strokeWidth = 4.dp
            )
        } else {
            MovieList(
                movies = movies,
                onMovieSelected = onMovieSelected,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {
    val fakeMovies = listOf(
        MovieItem(
            id = "1",
            title = "Doctor Strange",
            posterThumbnailUrl = ""
        ),
        MovieItem(
            id = "2",
            title = "The Imitation Game",
            posterThumbnailUrl = ""
        ),
        MovieItem(
            id = "3",
            title = "Sherlock Holmes",
            posterThumbnailUrl = ""
        )
    )

    MovieListScreenContent(
        modifier = Modifier,
        movies = fakeMovies,
        onMovieSelected = {},
    )
}