package com.cesarynga.pokedex.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonEntity
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonTypeCrossRef
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonTypeEntity
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonWithTypes
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(vararg pokemon: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonType(vararg pokemonType: PokemonTypeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonTypeCrossRef(pokemonTypeCrossRef: PokemonTypeCrossRef)

    @Query("SELECT * FROM Pokemon")
    fun observePokemons(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM Pokemon WHERE pokemonId = :pokemonId")
    fun observePokemonById(pokemonId: Int) : Flow<PokemonEntity>

    @Transaction
    @Query("SELECT * FROM Pokemon WHERE pokemonId = :pokemonId")
    fun getPokemonWithTypesById(pokemonId: Int) : PokemonWithTypes

    @Transaction
    @Query("SELECT * FROM Pokemon WHERE pokemonId = :pokemonId")
    fun observePokemonWithTypesById(pokemonId: Int) : Flow<PokemonWithTypes>
}