package com.burov.movie.discovery.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("3/discover/movie")
    suspend fun getMovies(
        @Query("with_people") personId: String
    ): MovieResponse

    @GET("3/movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: String,
    ): MovieResponse
}