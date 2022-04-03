package com.cesarynga.pokedex.data.source.remote

import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import java.lang.Exception

data class PokemonEntity(
    val name: String,
    val url: String
) {
    fun toPokemon():Pokemon {
        val id = try {
            url.split("/".toRegex()).dropLast(1).last().toInt()
        } catch (e: Exception) {
            -1
        }
        val imageUrl = if (id == -1) {
            ""
        } else {
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
        }
        return Pokemon(
            id = id,
            name = name,
            imageUrl = imageUrl
        )
    }
}
