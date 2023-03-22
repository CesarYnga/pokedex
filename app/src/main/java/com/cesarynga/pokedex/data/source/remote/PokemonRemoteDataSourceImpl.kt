package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import com.cesarynga.pokedex.data.source.remote.api.PokemonApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

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

    override suspend fun getPokemon(id: Int): PokemonModel = withContext(defaultDispatcher) {
        return@withContext pokemonApi.getPokemon(id).toPokemonModel()
    }
}