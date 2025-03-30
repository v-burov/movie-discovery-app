package com.burov.movie.discovery.app

import com.burov.movie.discovery.app.data.MovieRepository
import com.burov.movie.discovery.app.domain.movies.FetchMoviesUseCase
import com.burov.movie.discovery.app.domain.movies.SubscribeMoviesUseCase
import com.burov.movie.discovery.app.presentation.common.MovieItem
import com.burov.movie.discovery.app.presentation.movies.MovieListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.MockProvider
import org.koin.test.mock.declareMock
import kotlin.test.assertEquals
import com.burov.movie.discovery.app.domain.movies.MovieItem as DomainMovieItem

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest : KoinTest {

    private lateinit var movieListViewModel: MovieListViewModel

    private val movieRepository: MovieRepository = mockk()

    private val testModule = module {
        single<MovieRepository> { movieRepository }
        factory { FetchMoviesUseCase(get()) }
        factory { SubscribeMoviesUseCase(get()) }
        factory { MovieListViewModel(get(), get()) }
    }

    @Before
    fun setup() {
        MockProvider.register { movieRepository }
        Dispatchers.setMain(Dispatchers.Unconfined)

        startKoin {
            modules(testModule)
        }

        declareMock<MovieRepository> {
            coEvery { fetchMovies(any()) } returns Result.success(Unit)
            coEvery { observeMovies(any()) } returns flow {
                emit(
                    listOf(
                        DomainMovieItem("movie1", "Movie 1", "url1"),
                        DomainMovieItem("movie2", "Movie 2", "url2")
                    )
                )
            }
        }

        movieListViewModel = inject<MovieListViewModel>().value
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `test fetchMovies success`() = runTest {
        val personId = "123"
        val expectedMovies = listOf(
            MovieItem("movie1", "Movie 1", "url1")
        )

        val domainMovies = listOf(
            DomainMovieItem("movie1", "Movie 1", "url1")
        )

        coEvery { movieRepository.fetchMovies(personId) } returns Result.success(Unit)
        coEvery { movieRepository.observeMovies(personId) } returns flow {
            emit(domainMovies)
        }

        movieListViewModel.fetchMovies(personId)
        movieListViewModel.subscribeToMovies(personId)

        val movieList = movieListViewModel.data.first { it.isNotEmpty() }

        assertEquals(expectedMovies.size, movieList.size)
        assertEquals(expectedMovies, movieList)

        coVerify { movieRepository.fetchMovies(personId) }
        coVerify { movieRepository.observeMovies(personId) }
    }

    @Test
    fun `test fetchMovies failure`() = runTest {
        val personId = "123"
        coEvery { movieRepository.fetchMovies(personId) } returns Result.failure(Exception("Error fetching movies"))

        movieListViewModel.fetchMovies(personId)

        val errorMessage = movieListViewModel.error.first()

        assertEquals("Error fetching movies", errorMessage)
        coVerify { movieRepository.fetchMovies(personId) }
    }
}