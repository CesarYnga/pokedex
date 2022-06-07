package com.cesarynga.pokedex.pokemons.domain.model

data class PokemonPage(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)
