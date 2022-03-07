package com.cesarynga.pokedex.data.source.remote

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonEntity>
)
