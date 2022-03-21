package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class FakePokemonDataSource(var pokemonList: List<PokemonEntity>?) : PokemonDataSource {

    override fun getPokemonList(page: Int): Flow<List<PokemonEntity>> = flow {
        pokemonList?.let {
            return@flow emit(it)
        }
        throw Exception("Pokemons not found")
    }
}