package com.cesarynga.pokedex.data.source.remote

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import java.lang.Exception

data class PokemonResponse(
    val name: String,
    val url: String
) {
    fun toPokemon(): Pokemon {
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
            name = name.capitalize(Locale.current),
            imageUrl = imageUrl
        )
    }
}
