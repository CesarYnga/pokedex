package com.cesarynga.pokedex.data.source

import com.cesarynga.pokedex.data.source.remote.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonDataSource {

    fun getPokemonList(page: Int): Flow<List<PokemonEntity>>
}