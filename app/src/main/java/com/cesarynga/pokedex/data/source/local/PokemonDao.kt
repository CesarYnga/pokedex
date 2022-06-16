package com.cesarynga.pokedex.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemons(vararg pokemon: PokemonEntity)

    @Query("SELECT * FROM Pokemon")
    fun getAllPokemons(): List<PokemonEntity>
}