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

    override fun getPokemonsStream(): Flow<List<PokemonModel>> {
        return pokemonLocalDataSource.getPokemonsStream()
    }

    override fun getPokemonList(offset: Int): Flow<PokemonPageModel> {
        return pokemonRemoteDataSource.getPokemonList(offset).onEach {
            pokemonLocalDataSource.savePokemon(*it.results.toTypedArray())
        }
    }

    override fun getPokemonStream(pokemonId: Int): Flow<PokemonModel> {
        return pokemonLocalDataSource.getPokemonStream(pokemonId)
    }

    override suspend fun getPokemon(pokemonId: Int): PokemonModel {
        val pokemonModel = pokemonRemoteDataSource.getPokemon(pokemonId)
        pokemonLocalDataSource.savePokemonDetail(pokemonModel)
        return pokemonModel
    }
}