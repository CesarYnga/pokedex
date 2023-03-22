package com.cesarynga.pokedex.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cesarynga.pokedex.data.source.PokemonTypeModel

@Entity(tableName = "Type")
data class PokemonTypeEntity(
    @PrimaryKey  @ColumnInfo(name = "typeId")val id: Int,
    val name: String
) {
    fun toPokemonTypeModel(): PokemonTypeModel {
        return PokemonTypeModel(
            id = id,
            name = name
        )
    }
}
