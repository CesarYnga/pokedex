package com.cesarynga.pokedex.data.source.local

import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.local.db.PokemonDao
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonTypeCrossRef
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PokemonLocalDataSourceImpl(
    private val pokemonDao: PokemonDao,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonLocalDataSource {

    override fun getPokemonsStream(): Flow<List<PokemonModel>> {
        return pokemonDao.observePokemons().map { list ->
            list.map {
                it.toPokemonModel()
            }
        }.flowOn(defaultDispatcher)
    }

    override fun getPokemonStream(pokemonId: Int): Flow<PokemonModel> {
        return pokemonDao.observePokemonWithTypesById(pokemonId).map {
            it.toPokemonModel()
        }.flowOn(defaultDispatcher)
    }

    override suspend fun savePokemon(vararg pokemon: PokemonModel) =
        withContext(defaultDispatcher) {
            pokemonDao.insertPokemon(*pokemon.map {
                it.toPokemonEntity()
            }.toTypedArray())
        }

    override suspend fun savePokemonDetail(pokemon: PokemonModel) =
        withContext(defaultDispatcher) {
            pokemonDao.insertPokemonType(*pokemon.types.map {
                pokemonDao.insertPokemonTypeCrossRef(PokemonTypeCrossRef(pokemon.id, it.id))
                it.toPokemonTypeEntity()
            }.toTypedArray())
        }
}