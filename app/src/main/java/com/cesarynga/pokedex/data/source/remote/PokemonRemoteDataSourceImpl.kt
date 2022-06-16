package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PokemonRemoteDataSourceImpl(
    private val pokemonApi: PokemonApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonRemoteDataSource {

    override fun getPokemonList(offset: Int): Flow<PokemonPageModel> = flow {
        val pokemonPage = pokemonApi.getPokemonList(
            limit = PAGE_SIZE,
            offset = offset
        ).toPokemonPageModel()
        kotlinx.coroutines.delay(5000)
        emit(pokemonPage)
    }.flowOn(ioDispatcher)

    companion object {
        private const val PAGE_SIZE = 20
    }
}