package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.remote.PokemonPageResponse
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(page: Int): Flow<PokemonPageResponse>
}