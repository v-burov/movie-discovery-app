package com.burov.movie.discovery.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.compose.composable
import com.burov.movie.discovery.app.presentation.details.MovieDetailsScreen
import com.burov.movie.discovery.app.presentation.movies.MovieListScreen

internal const val KEY_MOVIE_ID = "movieId"

@Composable
fun MovieNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MovieListScreen.route) {
        composable(route = Screen.MovieListScreen.route) {
            MovieListScreen(onMovieSelected = { movieId ->
                navController.navigate(Screen.MovieDetailsScreen.createRoute(movieId))
            })
        }

        composable(
            route = Screen.MovieDetailsScreen.route,
            arguments = listOf(navArgument(KEY_MOVIE_ID) { type = NavType.StringType })
        ) {
            val movieId = it.arguments?.getString(KEY_MOVIE_ID).orEmpty()
            MovieDetailsScreen(
                movieId = movieId,
                onMovieSelected = { newMovieId ->
                    navController.navigate(Screen.MovieDetailsScreen.createRoute(newMovieId))
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}