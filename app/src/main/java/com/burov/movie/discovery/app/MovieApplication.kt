package com.burov.movie.discovery.app

import android.app.Application
import com.burov.movie.discovery.app.di.domainModule
import com.burov.movie.discovery.app.di.networkModule
import com.burov.movie.discovery.app.di.dataModule
import com.burov.movie.discovery.app.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MovieApplication)
            modules(networkModule, presentationModule, domainModule, dataModule)
        }
    }
}