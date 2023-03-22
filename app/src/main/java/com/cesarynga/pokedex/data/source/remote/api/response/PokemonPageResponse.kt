package com.cesarynga.pokedex.data.source.remote.api.response

import com.cesarynga.pokedex.data.source.PokemonPageModel

data class PokemonPageResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResponse>
) {
    fun toPokemonPageModel(): PokemonPageModel {
        return PokemonPageModel(
            hasNext = next != null,
            results = results.map {
                it.toPokemonModel()
            }
        )
    }
}
