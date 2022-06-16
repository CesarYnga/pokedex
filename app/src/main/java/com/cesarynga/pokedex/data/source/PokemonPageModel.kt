package com.cesarynga.pokedex.data.source

import com.cesarynga.pokedex.pokemons.domain.model.PokemonPage

data class PokemonPageModel(
    val hasNext: Boolean,
    val results: List<PokemonModel>
) {
    fun toPokemonPage(): PokemonPage {
        return PokemonPage(
            hasNext = hasNext,
            results = results.map {
                it.toPokemon()
            }
        )
    }
}
