package com.burov.movie.discovery.app

import com.burov.movie.discovery.app.data.MovieRepositoryImpl
import com.burov.movie.discovery.app.data.local.MovieDao
import com.burov.movie.discovery.app.data.local.MovieEntity
import com.burov.movie.discovery.app.data.remote.ApiService
import com.burov.movie.discovery.app.data.remote.MovieResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepositoryImpl
    private val apiService: ApiService = mockk()
    private val movieDao: MovieDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = MovieRepositoryImpl(apiService, movieDao)
    }

    private val mockMovies = listOf(
        MovieResult(
            id = "1",
            title = "Inception",
            overview = "A mind-bending thriller",
            posterPath = "posterThumbnailPath",
            posterThumbnailPath = "posterPath"
        ),
        MovieResult(
            id = "2",
            title = "Interstellar",
            overview = "A space exploration adventure",
            posterThumbnailPath = "posterThumbnailPath",
            posterPath = "posterPath"
        )
    )

    @Test
    fun `fetchMovies should return movie IDs on success`() = runTest {
        val personId = "123"
        coEvery { apiService.getMovies(personId) } returns mockk {
            every { movies } returns mockMovies
        }
        coEvery { movieDao.insertMovies(any()) } just Runs

        val result = repository.fetchMovies(personId)

        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `fetchMovies should return failure on API error`() = runTest {
        coEvery { apiService.getMovies(any()) } throws RuntimeException("API error")

        val result = repository.fetchMovies("123")

        assertTrue(result.isFailure)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `fetchSimilarMovies should return success`() = runTest {
        val movieId = "10"
        coEvery { apiService.getSimilarMovies(movieId) } returns mockk {
            every { movies } returns mockMovies
        }
        coEvery { movieDao.insertMovies(any()) } just Runs
        coEvery { movieDao.insertSimilarMovies(any()) } just Runs

        val result = repository.fetchSimilarMovies(movieId)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `observeMovieDetails should return mapped MovieDetails`() = runTest {
        val movieEntity = MovieEntity(
            id = "1",
            title = "Movie 1",
            overview = "Overview",
            posterThumbnailUrl = "thumbUrl",
            posterUrl = "posterUrl"
        )
        every { movieDao.getMovie("1") } returns flowOf(movieEntity)

        val movieDetails = repository.observeMovieDetails("1")
            .stateIn(this, SharingStarted.Eagerly, null)
            .first { it != null }

        assertNotNull(movieDetails)
        assertEquals("1", movieDetails?.id)
        assertEquals("Movie 1", movieDetails?.title)
        assertEquals("Overview", movieDetails?.overview)
        assertEquals("posterUrl", movieDetails?.posterUrl)
    }
}