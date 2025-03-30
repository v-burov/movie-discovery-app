package com.burov.movie.discovery.app.presentation.navigation

private const val ROUTE_MOVIE_LIST_SCREEN = "movie_list_screen"
private const val ROUTE_MOVIE_DETAILS_SCREEN = "movie_details_screen"

sealed class Screen(val route: String) {

    data object MovieListScreen : Screen(ROUTE_MOVIE_LIST_SCREEN)

    data object MovieDetailsScreen : Screen("$ROUTE_MOVIE_DETAILS_SCREEN/{$KEY_MOVIE_ID}") {
        fun createRoute(movieId: String) = "$ROUTE_MOVIE_DETAILS_SCREEN/$movieId"
    }
}