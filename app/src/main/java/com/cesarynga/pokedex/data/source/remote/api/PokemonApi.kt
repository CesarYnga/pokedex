package com.cesarynga.pokedex.data.source.remote.api

import com.cesarynga.pokedex.data.source.remote.api.response.PokemonPageResponse
import com.cesarynga.pokedex.data.source.remote.api.response.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ) : PokemonPageResponse

    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") pokemonId: Int): PokemonResponse
}