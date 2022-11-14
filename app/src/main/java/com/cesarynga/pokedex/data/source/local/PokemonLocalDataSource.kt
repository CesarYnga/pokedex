package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {
    fun getAllPokemons(): Flow<List<PokemonModel>>

    fun getPokemonById(pokemonId: Int): Flow<PokemonModel>

    suspend fun savePokemonList(pokemonList: List<PokemonModel>)
}