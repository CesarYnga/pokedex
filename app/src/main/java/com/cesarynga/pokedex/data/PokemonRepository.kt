package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemonList(offset: Int): Flow<PokemonPageModel>
}