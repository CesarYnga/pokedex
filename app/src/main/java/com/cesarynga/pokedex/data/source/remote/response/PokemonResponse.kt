package com.cesarynga.pokedex.data.source.remote

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.cesarynga.pokedex.data.source.PokemonModel

data class PokemonResponse(
    val name: String,
    val url: String
) {
    fun toPokemonModel(): PokemonModel {
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
        return PokemonModel(
            id = id,
            name = name.capitalize(Locale.current),
            imageUrl = imageUrl
        )
    }
}
