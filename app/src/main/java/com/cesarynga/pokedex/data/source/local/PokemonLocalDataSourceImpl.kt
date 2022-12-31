package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PokemonLocalDataSourceImpl(
    private val pokemonDao: PokemonDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonLocalDataSource {

    override fun getAllPokemons(): Flow<List<PokemonModel>> {
        return pokemonDao.getAllPokemons().map { list ->
            list.map {
                it.toPokemonModel()
            }
        }.flowOn(ioDispatcher)
    }

    override fun getPokemonById(pokemonId: Int): Flow<PokemonModel> {
        return pokemonDao.getPokemonById(pokemonId).map {
            it.toPokemonModel()
        }.flowOn(ioDispatcher)
    }

    override suspend fun savePokemonList(pokemonList: List<PokemonModel>) =
        withContext(ioDispatcher) {
            pokemonDao.insertPokemons(*pokemonList.map {
                it.toPokemonEntity()
            }.toTypedArray())
        }
}