package com.cesarynga.pokedex.data.source

import com.cesarynga.pokedex.data.source.local.db.entity.PokemonTypeEntity
import com.cesarynga.pokedex.pokemons.domain.model.PokemonType

data class PokemonTypeModel(val id: Int, val name: String) {
    fun toPokemonType(): PokemonType {
        return PokemonType(
            id = id,
            name = name
        )
    }

    fun toPokemonTypeEntity(): PokemonTypeEntity {
        return PokemonTypeEntity(
            id = id,
            name = name
        )
    }
}
