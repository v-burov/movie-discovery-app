package com.burov.movie.discovery.app.data

import com.burov.movie.discovery.app.BuildConfig
import com.burov.movie.discovery.app.data.local.MovieDao
import com.burov.movie.discovery.app.data.local.MovieEntity
import com.burov.movie.discovery.app.data.local.MovieToSimilar
import com.burov.movie.discovery.app.data.local.PersonEntity
import com.burov.movie.discovery.app.data.local.PersonToMovieEntity
import com.burov.movie.discovery.app.data.remote.ApiService
import com.burov.movie.discovery.app.domain.details.MovieDetails
import com.burov.movie.discovery.app.domain.movies.MovieItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

internal class MovieRepositoryImpl(
    private val apiService: ApiService,
    private val movieDao: MovieDao,
) : MovieRepository {

    override suspend fun fetchMovies(personId: String): Result<Unit> {
        return runCatching {
            val response = apiService.getMovies(personId = personId)
            val movies = response.movies.map { movieResponse ->
                MovieEntity(
                    id = movieResponse.id,
                    title = movieResponse.title,
                    overview = movieResponse.overview,
                    posterThumbnailUrl = movieResponse.posterThumbnailPath?.getThumbnailUrl(),
                    posterUrl = movieResponse.posterPath?.getPosterUrl()
                )
            }

            val person = PersonEntity(id = personId)
            val relations = movies.map { movie -> PersonToMovieEntity(personId, movie.id) }

            movieDao.insertPersonWithMovies(person, movies, relations)

            Result.success(Unit)
        }
    }

    override suspend fun fetchSimilarMovies(movieId: String): Result<Unit> = runCatching {
        val movieEntities = apiService.getSimilarMovies(movieId).movies.map { movie ->
            MovieEntity(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterThumbnailUrl = movie.posterThumbnailPath?.getThumbnailUrl(),
                posterUrl = movie.posterPath?.getPosterUrl()
            )
        }

        val similarMoviesRelations = movieEntities.map { movie ->
            MovieToSimilar(movieId = movieId, similarMovieId = movie.id)
        }

        movieDao.insertMovieWithSimilarMovies(movieEntities, similarMoviesRelations)
    }

    override fun observeMovies(personId: String): Flow<List<MovieItem>> {
        return movieDao.getPersonWithMovies(personId = personId)
            .flowOn(Dispatchers.IO)
            .map { movieEntities ->
                movieEntities?.movies?.map { movieEntity ->
                    MovieItem(
                        id = movieEntity.id,
                        title = movieEntity.title,
                        posterThumbnailUrl = movieEntity.posterThumbnailUrl,
                    )
                }?: emptyList()
            }
    }

    override fun observeSimilarMovies(movieId: String): Flow<List<MovieItem>> {
        return movieDao.getMovieWithSimilarMovies(movieId = movieId)
            .flowOn(Dispatchers.IO)
            .map { movieWithSimilarMovie ->
            movieWithSimilarMovie?.similarMovies?.map { movieEntity ->
                MovieItem(
                    id = movieEntity.id,
                    title = movieEntity.title,
                    posterThumbnailUrl = movieEntity.posterThumbnailUrl,
                )
            }?: emptyList()
        }
    }

    override fun observeMovieDetails(movieId: String): Flow<MovieDetails?> =
        movieDao.getMovie(movieId)
            .flowOn(Dispatchers.IO)
            .mapNotNull { movieEntity ->
            movieEntity?.let {
                MovieDetails(
                    id = it.id,
                    title = it.title,
                    overview = it.overview,
                    posterUrl = it.posterUrl
                )
            }
        }

    private fun String.getPosterUrl(): String = "${BuildConfig.POSTER_BASE_PATH}$this.jpg"

    private fun String.getThumbnailUrl(): String = "${BuildConfig.POSTER_THUMBNAIL_PATH}$this.jpg"
}