package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {
    fun getPokemonsStream(): Flow<List<PokemonModel>>

    fun getPokemonStream(pokemonId: Int): Flow<PokemonModel>

    suspend fun savePokemon(vararg pokemon: PokemonModel)

    suspend fun savePokemonDetail(pokemon: PokemonModel)
}