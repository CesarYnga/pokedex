package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.data.source.PokemonDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PokemonRemoteDataSource(
    private val pokemonApi: PokemonApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonDataSource {

    override fun getPokemonList(offset: Int): Flow<PokemonPageResponse> = flow {
        val pokemonPage = pokemonApi.getPokemonList(
            limit = PAGE_SIZE,
            offset = offset
        )
        emit(pokemonPage)
    }.flowOn(ioDispatcher)

    companion object {
        private const val PAGE_SIZE = 20
    }
}