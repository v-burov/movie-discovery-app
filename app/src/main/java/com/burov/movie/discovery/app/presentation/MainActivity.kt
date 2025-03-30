package com.burov.movie.discovery.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.burov.movie.discovery.app.presentation.navigation.MovieNavigation
import com.burov.movie.discovery.app.presentation.theme.MovieDiscoveryAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDiscoveryAppTheme {
                MovieNavigation()
            }
        }
    }
}