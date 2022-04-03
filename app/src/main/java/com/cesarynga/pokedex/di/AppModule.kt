package com.cesarynga.pokedex.di

import com.cesarynga.pokedex.data.PokemonRepository
import com.cesarynga.pokedex.data.PokemonRepositoryImpl
import com.cesarynga.pokedex.data.source.PokemonDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonApi
import com.cesarynga.pokedex.data.source.remote.PokemonRemoteDataSource
import com.cesarynga.pokedex.pokemons.PokemonListViewModel
import com.cesarynga.pokedex.pokemons.domain.usecase.GetPokemonListUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    viewModel { PokemonListViewModel(get()) }

    factory { GetPokemonListUseCase(get()) }

    single<PokemonRepository> { PokemonRepositoryImpl(get(named("pokemonRemoteDataSource"))) }

    single<PokemonDataSource>(named("pokemonRemoteDataSource")) { PokemonRemoteDataSource(get()) }

    single {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }
}