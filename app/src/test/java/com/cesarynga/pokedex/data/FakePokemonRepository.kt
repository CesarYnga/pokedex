package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.remote.PokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class FakePokemonRepository(var pokemonList: List<PokemonEntity>?) : PokemonRepository {

    override fun getPokemonList(page: Int): Flow<List<PokemonEntity>> = flow {
        pokemonList?.let {
            return@flow emit(it)
        }
        throw Exception("Pokemons not found")
    }
}