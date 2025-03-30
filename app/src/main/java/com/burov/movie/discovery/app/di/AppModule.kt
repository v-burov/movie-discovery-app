package com.burov.movie.discovery.app.di

import android.app.Application
import androidx.room.Room
import com.burov.movie.discovery.app.BuildConfig
import com.burov.movie.discovery.app.data.MovieRepository
import com.burov.movie.discovery.app.data.MovieRepositoryImpl
import com.burov.movie.discovery.app.data.local.MovieDao
import com.burov.movie.discovery.app.data.local.MovieDatabase
import com.burov.movie.discovery.app.data.remote.ApiService
import com.burov.movie.discovery.app.di.ApiHeaders.ACCEPT_TYPE
import com.burov.movie.discovery.app.domain.details.FetchSimilarMoviesUseCase
import com.burov.movie.discovery.app.domain.details.SubscribeMovieDetailsUseCase
import com.burov.movie.discovery.app.domain.details.SubscribeSimilarMovieUseCase
import com.burov.movie.discovery.app.domain.movies.FetchMoviesUseCase
import com.burov.movie.discovery.app.domain.movies.SubscribeMoviesUseCase
import com.burov.movie.discovery.app.presentation.details.MovieDetailsViewModel
import com.burov.movie.discovery.app.presentation.movies.MovieListViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

object ApiHeaders {
    const val ACCEPT = "Accept"
    const val AUTHORIZATION = "Authorization"
    const val ACCEPT_TYPE = "application/json"
}

private const val DATABASE_NAME = "movie_database"

val networkModule = module {
    single { AuthInterceptor() }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Json>().asConverterFactory(ACCEPT_TYPE.toMediaType()))
            .build()
    }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}

val presentationModule = module {
    viewModel { MovieListViewModel(get(), get()) }
    viewModel { MovieDetailsViewModel(get(), get(), get()) }
}

val domainModule = module {
    factory { FetchMoviesUseCase(get()) }
    factory { FetchSimilarMoviesUseCase(get()) }
    factory { SubscribeMoviesUseCase(get()) }
    factory { SubscribeMovieDetailsUseCase(get()) }
    factory { SubscribeSimilarMovieUseCase(get()) }
}

val dataModule = module {
    fun provideDataBase(application: Application): MovieDatabase =
        Room.databaseBuilder(
            context = application,
            klass = MovieDatabase::class.java,
            name = DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    fun provideDao(postDataBase: MovieDatabase): MovieDao = postDataBase.movieDao()

    single { provideDataBase(get()) }
    single { provideDao(get()) }
    factory<MovieRepository> { MovieRepositoryImpl(get(), get()) }
}