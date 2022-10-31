package com.cesarynga.pokedex

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import com.cesarynga.pokedex.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class PokemonApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Koin Android logger
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            //inject Android context
            androidContext(this@PokemonApp)
            // use modules
            modules(appModule)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .logger(if (BuildConfig.DEBUG) DebugLogger() else null)
            .build()
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}