package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonsStream(): Flow<List<PokemonModel>>

    fun getPokemonList(offset: Int): Flow<PokemonPageModel>

    fun getPokemonStream(pokemonId: Int): Flow<PokemonModel>

    suspend fun getPokemon(pokemonId: Int): PokemonModel
}