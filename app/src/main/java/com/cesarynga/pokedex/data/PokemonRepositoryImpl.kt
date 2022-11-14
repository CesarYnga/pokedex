package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import com.cesarynga.pokedex.data.source.local.PokemonLocalDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonRemoteDataSource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class)
class PokemonRepositoryImpl(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource,
    private val pokemonLocalDataSource: PokemonLocalDataSource
) : PokemonRepository {

    override fun getPokemonList(offset: Int): Flow<PokemonPageModel> {
        return if (offset == 0) {
            val localPokemons = pokemonLocalDataSource.getAllPokemons()
            localPokemons.flatMapMerge { pokemonList ->
                if (pokemonList.isEmpty()) {
                    loadPokemonsFromRemoteDataSource(0)
                } else {
                    localPokemons.map { pokemonModel ->
                        PokemonPageModel(true, pokemonModel)
                    }
                }
            }
        } else {
            loadPokemonsFromRemoteDataSource(offset)
        }
    }

    override fun getPokemonById(pokemonId: Int): Flow<PokemonModel> =
        pokemonLocalDataSource.getPokemonById(pokemonId)

    private fun loadPokemonsFromRemoteDataSource(offset: Int): Flow<PokemonPageModel> {
        return pokemonRemoteDataSource.getPokemonList(offset).onEach {
            pokemonLocalDataSource.savePokemonList(it.results)
        }
    }
}