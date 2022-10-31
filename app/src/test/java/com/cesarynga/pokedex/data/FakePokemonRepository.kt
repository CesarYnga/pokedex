package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import com.cesarynga.pokedex.data.source.remote.PokemonResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePokemonRepository(var pokemonList: List<PokemonModel>?, var hasNextPage: Boolean) :
    PokemonRepository {

    override fun getPokemonList(page: Int): Flow<PokemonPageModel> = flow {
        delay(3_000)
        if (pokemonList != null) {
            emit(PokemonPageModel(hasNextPage, pokemonList!!))
        } else {
            throw Exception("Pokemons not found")
        }
    }
}