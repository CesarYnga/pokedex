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

    override fun getPokemonList(page: Int): Flow<List<PokemonEntity>> = flow {
        val pokemonList = pokemonApi.getPokemonList(
            limit = PAGE_SIZE,
            offset = page * PAGE_SIZE
        ).results
        emit(pokemonList)
    }.flowOn(ioDispatcher)

    companion object {
        private const val PAGE_SIZE = 20
    }
}