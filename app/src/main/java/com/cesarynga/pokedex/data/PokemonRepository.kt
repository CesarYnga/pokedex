package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun observePokemons(): Flow<List<PokemonModel>>

    fun getPokemonList(offset: Int): Flow<PokemonPageModel>

    fun getPokemonById(pokemonId: Int): Flow<PokemonModel>
}