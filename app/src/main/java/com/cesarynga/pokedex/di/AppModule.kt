package com.cesarynga.pokedex.di

import androidx.room.Room
import com.cesarynga.pokedex.data.PokemonRepository
import com.cesarynga.pokedex.data.PokemonRepositoryImpl
import com.cesarynga.pokedex.data.source.local.PokemonDatabase
import com.cesarynga.pokedex.data.source.local.PokemonLocalDataSource
import com.cesarynga.pokedex.data.source.local.PokemonLocalDataSourceImpl
import com.cesarynga.pokedex.data.source.remote.PokemonApi
import com.cesarynga.pokedex.data.source.remote.PokemonRemoteDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonRemoteDataSourceImpl
import com.cesarynga.pokedex.pokemons.PokemonListViewModel
import com.cesarynga.pokedex.pokemons.domain.usecase.GetPokemonListUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    viewModel { PokemonListViewModel(get()) }

    factory { GetPokemonListUseCase(get()) }

    single<PokemonRepository> {
        PokemonRepositoryImpl(
            get(named("pokemonRemoteDataSource")),
            get(named("pokemonLocalDataSource"))
        )
    }

    single<PokemonRemoteDataSource>(named("pokemonRemoteDataSource")) {
        PokemonRemoteDataSourceImpl(
            get()
        )
    }

    single<PokemonLocalDataSource>(named("pokemonLocalDataSource")) {
        PokemonLocalDataSourceImpl(
            get<PokemonDatabase>().pokemonDao()
        )
    }

    single{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            PokemonDatabase::class.java, "pokemon-db"
        ).build()
    }
}