package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import com.cesarynga.pokedex.data.source.local.PokemonLocalDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class PokemonRepositoryImpl(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource,
    private val pokemonLocalDataSource: PokemonLocalDataSource
) : PokemonRepository {

    override fun observePokemons(): Flow<List<PokemonModel>> {
        return pokemonLocalDataSource.getAllPokemons()
    }

    override fun getPokemonList(offset: Int): Flow<PokemonPageModel> {
        return pokemonRemoteDataSource.getPokemonList(offset).onEach {
            pokemonLocalDataSource.savePokemonList(it.results)
        }
    }

    override fun getPokemon(id: Int): Flow<PokemonModel> =
        pokemonLocalDataSource.getPokemonById(id)
}