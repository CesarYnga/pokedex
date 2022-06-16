package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PokemonLocalDataSourceImpl(
    private val pokemonDb: PokemonDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonLocalDataSource {

    override fun getAllPokemons(): Flow<PokemonPageModel> = flow {
        val pokemonList = pokemonDb.pokemonDao().getAllPokemons().map {
            it.toPokemonModel()
        }
        emit(PokemonPageModel(true, pokemonList))
    }.flowOn(ioDispatcher)

    override suspend fun savePokemonList(pokemonList: List<PokemonModel>) = withContext(ioDispatcher) {
        pokemonDb.pokemonDao().insertPokemons(*pokemonList.map {
            it.toPokemonEntity()
        }.toTypedArray())
    }
}