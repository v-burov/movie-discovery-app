package com.burov.movie.discovery.app.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.burov.movie.discovery.app.R
import com.burov.movie.discovery.app.presentation.common.LoadImageFromUrl
import com.burov.movie.discovery.app.presentation.common.MovieList
import com.burov.movie.discovery.app.presentation.common.MovieItem
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: String,
    onMovieSelected: (String) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = koinViewModel(),
) {
    val movieDetails by viewModel.data.collectAsState()
    val similarMovies by viewModel.similarMovies.collectAsState()
    val error by viewModel.error.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(movieId) {
        viewModel.run {
            subscribeToMovieDetails(movieId = movieId)
            subscribeToSimilarMovies(movieId = movieId)
            fetchSimilarMovies(movieId = movieId)
        }
    }

    LaunchedEffect(error) {
        error?.let { snackBarHostState.showSnackbar(it) }
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.movie_details)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { internalPadding ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(internalPadding)
            ) {
                if (movieDetails == null) {
                    Text(
                        text = stringResource(R.string.loading),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    movieDetails?.let { movie ->
                        MovieDetailsContent(
                            movie = movie,
                            similarMovies = similarMovies,
                            onMovieSelected = onMovieSelected,
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun MovieDetailsContent(
    movie: MovieDetails,
    similarMovies: List<MovieItem>,
    onMovieSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier,
        )

        Spacer(modifier = Modifier.height(16.dp))

        movie.posterUrl?.let {
            LoadImageFromUrl(
                imageUrl = it,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.movie_overview_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.similar_movies),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        MovieList(
            movies = similarMovies,
            onMovieSelected = onMovieSelected,
            modifier = Modifier
        )
    }
}