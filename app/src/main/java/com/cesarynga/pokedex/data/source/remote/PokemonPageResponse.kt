package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.pokemons.domain.model.PokemonPage

data class PokemonPageResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResponse>
) {
    fun toPokemonPage(): PokemonPage {
        return PokemonPage(
            count = count,
            next = next,
            previous = previous,
            results = results.map {
                it.toPokemon()
            }
        )
    }
}
