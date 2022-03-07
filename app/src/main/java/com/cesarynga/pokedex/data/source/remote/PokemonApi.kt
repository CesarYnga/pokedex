package com.cesarynga.pokedex.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun pokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 20,
    ) : PokemonListResponse
}