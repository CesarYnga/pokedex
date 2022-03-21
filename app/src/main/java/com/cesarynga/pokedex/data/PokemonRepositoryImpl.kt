package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonEntity
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImpl(private val pokemonRemoteDataSource: PokemonDataSource) : PokemonRepository {

    override fun getPokemonList(page: Int): Flow<List<PokemonEntity>> {
        return pokemonRemoteDataSource.getPokemonList(page)
    }
}