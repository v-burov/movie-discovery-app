package com.burov.movie.discovery.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM people WHERE id = :personId")
    fun getPersonWithMovies(personId: String): Flow<PersonWithMovies?>

    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    fun getMovie(movieId: String): Flow<MovieEntity?>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieWithSimilarMovies(movieId: String): Flow<MovieWithSimilarMovies?>

    @Transaction
    suspend fun insertPersonWithMovies(
        person: PersonEntity,
        movies: List<MovieEntity>,
        relations: List<PersonToMovieEntity>
    ) {
        insertPerson(person)
        insertMovies(movies)
        insertPersonToMovie(relations)
    }

    @Transaction
    suspend fun insertMovieWithSimilarMovies(
        movies: List<MovieEntity>,
        similarMovies: List<MovieToSimilar>
    ) {
        insertMovies(movies)
        insertSimilarMovies(similarMovies)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonToMovie(personEntity: List<PersonToMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimilarMovies(similarMovies: List<MovieToSimilar>)
}