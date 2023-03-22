package com.cesarynga.pokedex.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["pokemonId", "typeId"])
data class PokemonTypeCrossRef(
    val pokemonId: Int,
    @ColumnInfo(index = true)
    val typeId: Int
)
