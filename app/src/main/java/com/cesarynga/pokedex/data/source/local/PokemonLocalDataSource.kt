package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {
    fun getAllPokemons(): Flow<PokemonPageModel>

    suspend fun savePokemonList(pokemonList: List<PokemonModel>)
}