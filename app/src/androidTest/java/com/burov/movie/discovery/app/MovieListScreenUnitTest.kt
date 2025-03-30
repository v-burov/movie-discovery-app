package com.burov.movie.discovery.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.burov.movie.discovery.app.data.MovieRepository
import com.burov.movie.discovery.app.domain.movies.FetchMoviesUseCase
import com.burov.movie.discovery.app.domain.movies.SubscribeMoviesUseCase
import com.burov.movie.discovery.app.presentation.movies.MovieListScreen
import com.burov.movie.discovery.app.presentation.movies.MovieListViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import com.burov.movie.discovery.app.domain.movies.MovieItem as DomainMovieItem

class MovieListScreenUnitTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val movieRepository: MovieRepository by inject()

    private val viewModel: MovieListViewModel by inject()

    private val testModule = module {
        single<MovieRepository> { FakeMovieRepository() }
        factory { FetchMoviesUseCase(get()) }
        factory { SubscribeMoviesUseCase(get()) }
        viewModel { MovieListViewModel(get(), get()) }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(testModule)
    )

    private val fakeMovies = listOf(
        DomainMovieItem("movie1", "Doctor Strange", "url1"),
        DomainMovieItem(
            id = "movie2",
            title = "The Imitation Game",
            posterThumbnailUrl = "url2"
        )
    )

    @Test
    fun movieListScreen_displaysMovies() {
        (movieRepository as FakeMovieRepository).moviesFlow.value = fakeMovies

        composeTestRule.setContent {
            MovieListScreen(
                onMovieSelected = {},
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }

        composeTestRule.onNodeWithText("Doctor Strange").assertIsDisplayed()
    }

    @Test
    fun movieListScreen_displaysSnackbarOnError() {
        val errorMessage = "Failed to load movies"

        (movieRepository as FakeMovieRepository).errorFlow.value = Exception(errorMessage)

        composeTestRule.setContent {
            MovieListScreen(
                onMovieSelected = {},
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun movieListScreen_navigatesOnMovieClick() {
        var selectedMovieId: String? = null

        (movieRepository as FakeMovieRepository).moviesFlow.value = fakeMovies

        composeTestRule.setContent {
            MovieListScreen(
                onMovieSelected = { selectedMovieId = it },
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }

        composeTestRule.onNodeWithText("Doctor Strange").performClick()

        Assert.assertEquals("movie1", selectedMovieId)
    }
}