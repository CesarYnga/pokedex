package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonPageResponse
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl(private val pokemonRemoteDataSource: PokemonDataSource) : PokemonRepository {

    override fun getPokemonList(offset: Int): Flow<PokemonPageResponse> {
        return pokemonRemoteDataSource.getPokemonList(offset)
    }
}