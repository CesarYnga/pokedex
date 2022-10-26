package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PokemonLocalDataSourceImpl(
    private val pokemonDao: PokemonDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonLocalDataSource {

    override fun getAllPokemons(): Flow<List<PokemonModel>> = flow {
        val pokemonList = pokemonDao.getAllPokemons().map {
            it.toPokemonModel()
        }
        emit(pokemonList)
    }.flowOn(ioDispatcher)

    override suspend fun savePokemonList(pokemonList: List<PokemonModel>) = withContext(ioDispatcher) {
        pokemonDao.insertPokemons(*pokemonList.map {
            it.toPokemonEntity()
        }.toTypedArray())
    }
}