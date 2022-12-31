package com.cesarynga.pokedex.pokemons.domain.model

data class PokemonPage(
    val hasNext: Boolean,
    val results: List<Pokemon>
)
