package com.cesarynga.pokedex.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cesarynga.pokedex.data.source.PokemonModel

@Entity(tableName = "Pokemon")
data class PokemonEntity(
    @PrimaryKey @ColumnInfo(name = "pokemonId") val id: Int,
    val name: String,
    val imageUrl: String
) {
    fun toPokemonModel(): PokemonModel {
        return PokemonModel(
            id = id,
            name = name,
            imageUrl = imageUrl,
            types = emptyList()
        )
    }
}
