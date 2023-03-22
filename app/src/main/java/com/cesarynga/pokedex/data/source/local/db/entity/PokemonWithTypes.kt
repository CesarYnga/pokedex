package com.cesarynga.pokedex.data.source.local.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cesarynga.pokedex.data.source.PokemonModel

data class PokemonWithTypes(
    @Embedded val pokemon: PokemonEntity,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "typeId",
        associateBy = Junction(PokemonTypeCrossRef::class)
    )
    val types: List<PokemonTypeEntity>
) {
    fun toPokemonModel(): PokemonModel {
        return PokemonModel(
            id = pokemon.id,
            name = pokemon.name,
            imageUrl = pokemon.imageUrl,
            types = types.map { it.toPokemonTypeModel() }
        )
    }
}
