package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PokemonRemoteDataSourceImpl(
    private val pokemonApi: PokemonApi,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonRemoteDataSource {

    override fun getPokemonList(offset: Int, pageSize: Int): Flow<PokemonPageModel> = flow {
        val pokemonPage = pokemonApi.getPokemonList(
            limit = pageSize,
            offset = offset
        ).toPokemonPageModel()
        emit(pokemonPage)
    }.flowOn(defaultDispatcher)

    override fun getPokemon(id: Int): Flow<PokemonModel> = flow {
        val pokemon = pokemonApi.getPokemon(id).toPokemonModel()
        emit(pokemon)
    }.flowOn(defaultDispatcher)
}