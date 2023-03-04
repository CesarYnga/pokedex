package com.cesarynga.pokedex.data.source

import com.cesarynga.pokedex.pokemons.domain.model.PokemonType

data class PokemonTypeModel(val id: Int, val name: String) {
    fun toPokemonType(): PokemonType {
        return PokemonType(
            id = id,
            name = name
        )
    }
}
