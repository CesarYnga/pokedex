package com.cesarynga.pokedex.data.source

import com.cesarynga.pokedex.data.source.remote.PokemonPageResponse
import kotlinx.coroutines.flow.Flow

interface PokemonDataSource {

    fun getPokemonList(page: Int): Flow<PokemonPageResponse>
}