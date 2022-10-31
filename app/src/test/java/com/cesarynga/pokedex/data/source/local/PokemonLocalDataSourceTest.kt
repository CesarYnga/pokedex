package com.cesarynga.pokedex.data.source.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cesarynga.pokedex.MainCoroutineRule
import com.cesarynga.pokedex.data.source.PokemonModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@Config(sdk = [Build.VERSION_CODES.S_V2])
@RunWith(AndroidJUnit4::class)
class PokemonLocalDataSourceTest {

    private lateinit var pokemonLocalDataSource: PokemonLocalDataSource
    private lateinit var database: PokemonDatabase

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokemonDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        pokemonLocalDataSource = PokemonLocalDataSourceImpl(database.pokemonDao(), Dispatchers.Main)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Given several pokemons saved in the database, when pokemons retrieved, then same pokemons are returned`() =
        runTest {
            val newPokemon = PokemonModel(7, "Pokemon 7", "http://test-url.com/7")
            val newPokemon2 = PokemonModel(55, "Pokemon 55", "http://test-url.com/55")
            val newPokemon3 = PokemonModel(100, "Pokemon 100", "http://test-url.com/100")
            val newPokemonList = listOf(newPokemon, newPokemon2, newPokemon3)
            pokemonLocalDataSource.savePokemonList(newPokemonList)

            val result = pokemonLocalDataSource.getAllPokemons().first()

            assertThat(result.size).isEqualTo(newPokemonList.size)
            assertThat(result).isEqualTo(newPokemonList)
        }
}