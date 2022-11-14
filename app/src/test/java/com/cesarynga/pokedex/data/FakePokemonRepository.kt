package com.cesarynga.pokedex.data

import androidx.annotation.VisibleForTesting
import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePokemonRepository : PokemonRepository {

    private var hasNextPage = false
    private var shouldReturnError = false
    private val pokemonList = mutableListOf<PokemonModel>()

    override fun getPokemonList(page: Int): Flow<PokemonPageModel> = flow {
        if (!shouldReturnError) {
            emit(PokemonPageModel(hasNextPage, pokemonList))
        } else {
            throw Exception("Pokemons not found")
        }
    }

    override fun getPokemonById(pokemonId: Int): Flow<PokemonModel> = flow {
        if (!shouldReturnError) {
            val pokemonFound = pokemonList.find { it.id == pokemonId }
            if (pokemonFound != null) {
                emit(pokemonFound)
            } else {
                throw Exception("Pokemon with id $pokemonId not found")
            }
        } else {
            throw Exception("Pokemons not found")
        }
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    @VisibleForTesting
    fun addPokemons(vararg pokemons: PokemonModel) {
        pokemonList.addAll(pokemons)
    }

    @VisibleForTesting
    fun clearPokemons() {
        pokemonList.clear()
    }
}