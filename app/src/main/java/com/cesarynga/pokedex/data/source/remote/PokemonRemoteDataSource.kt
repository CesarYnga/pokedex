package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.data.source.PokemonPageModel
import kotlinx.coroutines.flow.Flow

interface PokemonRemoteDataSource {
    fun getPokemonList(offset: Int, pageSize: Int = PAGE_SIZE_DEFAULT): Flow<PokemonPageModel>

    companion object {
        private const val PAGE_SIZE_DEFAULT = 20
    }
}