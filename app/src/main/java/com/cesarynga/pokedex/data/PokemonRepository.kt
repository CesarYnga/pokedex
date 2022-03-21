package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.remote.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(page: Int): Flow<List<PokemonEntity>>
}