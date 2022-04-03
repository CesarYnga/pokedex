package com.cesarynga.pokedex.data.source

import com.cesarynga.pokedex.data.source.remote.PokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class FakePokemonDataSource(var pokemonList: List<PokemonEntity>?) : PokemonDataSource {

    override fun getPokemonList(page: Int): Flow<List<PokemonEntity>> = flow {
        pokemonList?.let {
            return@flow emit(it)
        }
        throw Exception("Pokemons not found")
    }
}