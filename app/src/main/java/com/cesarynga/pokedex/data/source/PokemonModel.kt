package com.cesarynga.pokedex.data.source

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.cesarynga.pokedex.data.source.local.PokemonEntity
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon

data class PokemonModel(
    val id: Int,
    val name: String,
    val imageUrl: String
) {
    fun toPokemon(): Pokemon {
        return Pokemon(
            id = id,
            name = name.capitalize(Locale.current),
            imageUrl = imageUrl
        )
    }

    fun toPokemonEntity() : PokemonEntity {
        return PokemonEntity(
            id = id,
            name = name,
            imageUrl = imageUrl
        )
    }
}
