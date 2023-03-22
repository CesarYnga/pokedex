package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePokemonLocalDataSource(var pokemonList: MutableList<PokemonModel> = mutableListOf()) :
    PokemonLocalDataSource {

    override fun getPokemonsStream(): Flow<List<PokemonModel>> = flow {
        emit(pokemonList)
    }

    override fun getPokemonStream(pokemonId: Int): Flow<PokemonModel> = flow {
        val pokemonFound = pokemonList.find { it.id == pokemonId }
        if (pokemonFound != null) {
            emit(pokemonFound)
        } else {
            throw Exception("Pokemon with id $pokemonId not found")
        }
    }

    override suspend fun savePokemon(pokemonList: List<PokemonModel>) {
        this.pokemonList.addAll(pokemonList)
    }
}