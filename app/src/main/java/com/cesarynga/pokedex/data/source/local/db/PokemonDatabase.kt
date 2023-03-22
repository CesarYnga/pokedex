package com.cesarynga.pokedex.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonEntity
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonTypeCrossRef
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonTypeEntity

@Database(entities = [PokemonEntity::class, PokemonTypeEntity::class, PokemonTypeCrossRef::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}