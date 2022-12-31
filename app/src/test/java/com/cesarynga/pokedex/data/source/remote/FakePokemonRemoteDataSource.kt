package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePokemonRemoteDataSource(
    var pokemonList: List<PokemonModel>?,
    var hasNextPage: Boolean
) : PokemonRemoteDataSource {

    override fun getPokemonList(page: Int, pageSize: Int): Flow<PokemonPageModel> = flow {
        if (pokemonList != null) {
            emit(PokemonPageModel(hasNextPage, pokemonList!!))
        } else {
            throw Exception("Pokemons not found")
        }
    }
}