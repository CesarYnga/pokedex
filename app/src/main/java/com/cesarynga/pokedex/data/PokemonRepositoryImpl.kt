package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonPageModel
import com.cesarynga.pokedex.data.source.local.PokemonLocalDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach

class PokemonRepositoryImpl(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource,
    private val pokemonLocalDataSource: PokemonLocalDataSource
) : PokemonRepository {

    override fun getPokemonList(offset: Int): Flow<PokemonPageModel> {

        return if (offset == 0) {
            val localPokemons = pokemonLocalDataSource.getAllPokemons()
            localPokemons.flatMapMerge {
                if (it.results.isEmpty()) {
                    loadPokemonsFromRemoteDataSource(0)
                } else {
                    localPokemons
                }
            }
        } else {
            loadPokemonsFromRemoteDataSource(offset)
        }
    }

    private fun loadPokemonsFromRemoteDataSource(offset: Int): Flow<PokemonPageModel> {
        return pokemonRemoteDataSource.getPokemonList(offset).onEach {
            pokemonLocalDataSource.savePokemonList(it.results)
        }
    }
}