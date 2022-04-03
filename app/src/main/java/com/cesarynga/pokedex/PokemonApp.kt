package com.cesarynga.pokedex

import android.app.Application
import com.cesarynga.pokedex.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PokemonApp : Application() {

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
}