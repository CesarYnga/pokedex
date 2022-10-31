package com.cesarynga.pokedex.data.source.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cesarynga.pokedex.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class PokemonDaoTest {

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
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Given pokemons inserted, when getting all pokemons, then returns the pokemons inserted`() =
        runTest {
            val pokemon = PokemonEntity(1, "Pokemon 1", "http://test-url.com/1")
            val pokemon2 = PokemonEntity(2, "Pokemon 2", "http://test-url.com/2")
            val pokemon3 = PokemonEntity(3, "Pokemon 3", "http://test-url.com/3")
            database.pokemonDao().insertPokemons(pokemon, pokemon2, pokemon3)

            val pokemonLoaded = database.pokemonDao().getAllPokemons()

            assertThat(pokemonLoaded.size).isEqualTo(3)
            assertThat(pokemonLoaded).contains(pokemon)
            assertThat(pokemonLoaded).contains(pokemon2)
            assertThat(pokemonLoaded).contains(pokemon3)
        }

    @Test
    fun `Given a pokemon inserted, when a pokemon with same id is inserted, then replace the previous pokemon and return the new one`() =
        runTest {
            val pokemon = PokemonEntity(1, "Pokemon 1", "http://test-url.com/1")
            database.pokemonDao().insertPokemons(pokemon)

            val newPokemon = PokemonEntity(1, "New Pokemon", "http://test-url.com/152")
            database.pokemonDao().insertPokemons(newPokemon)

            val pokemonLoaded = database.pokemonDao().getAllPokemons()

            assertThat(pokemonLoaded.size).isEqualTo(1)
            assertThat(pokemonLoaded[0].id).isEqualTo(pokemon.id)
            assertThat(pokemonLoaded[0].name).isEqualTo(newPokemon.name)
            assertThat(pokemonLoaded[0].imageUrl).isEqualTo(newPokemon.imageUrl)
        }
}