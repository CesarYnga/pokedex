package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.remote.PokemonResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class FakePokemonRepository(var pokemonList: List<PokemonResponse>?) : PokemonRepository {

    override fun getPokemonList(page: Int): Flow<List<PokemonResponse>> = flow {
        delay(3_000)
        pokemonList?.let {
            return@flow emit(it)
        }
        throw Exception("Pokemons not found")
    }
}