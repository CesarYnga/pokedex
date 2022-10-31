package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePokemonLocalDataSource(var pokemonList: MutableList<PokemonModel> = mutableListOf()) :
    PokemonLocalDataSource {

    override fun getAllPokemons(): Flow<List<PokemonModel>> = flow {
        emit(pokemonList)
    }

    override suspend fun savePokemonList(pokemonList: List<PokemonModel>) {
        this.pokemonList.addAll(pokemonList)
    }
}