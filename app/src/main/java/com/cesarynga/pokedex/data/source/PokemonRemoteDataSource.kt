package com.cesarynga.pokedex.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRemoteDataSource(
    private val pokemonApi: PokemonApi
) {
    fun pokemonList(page: Int): Flow<List<PokemonEntity>> = flow {
        val pokemons = pokemonApi.pokemonList(
            limit = PAGE_SIZE,
            offset = page * PAGE_SIZE
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}