package com.cesarynga.pokedex.data.source.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cesarynga.pokedex.MainCoroutineRule
import com.cesarynga.pokedex.PokemonTestApp
import com.cesarynga.pokedex.data.source.local.db.PokemonDatabase
import com.cesarynga.pokedex.data.source.local.db.entity.PokemonEntity
import com.google.common.truth.Truth.assertThat
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
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.S_V2], application = PokemonTestApp::class)
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
    fun `Given a pokemon inserted, when getting pokemon by id, then returns the pokemon inserted`() =
        runTest {
            val pokemon = PokemonEntity(1, "Pokemon 1", "http://test-url.com/1")
            database.pokemonDao().insertPokemon(pokemon)

            val pokemonLoaded = database.pokemonDao().observePokemonById(pokemon.id).first()

            assertThat(pokemonLoaded).isEqualTo(pokemon)
        }

    @Test
    fun `Given a pokemon inserted, when a pokemon with same id is inserted, then replace the previous pokemon and return the new one`() =
        runTest {
            val pokemon = PokemonEntity(1, "Pokemon 1", "http://test-url.com/1")
            database.pokemonDao().insertPokemon(pokemon)

            val newPokemon = PokemonEntity(1, "New Pokemon", "http://test-url.com/152")
            database.pokemonDao().insertPokemon(newPokemon)

            val pokemonLoaded = database.pokemonDao().observePokemonById(pokemon.id).first()

            assertThat(pokemonLoaded.id).isEqualTo(pokemon.id)
            assertThat(pokemonLoaded.name).isEqualTo(newPokemon.name)
            assertThat(pokemonLoaded.imageUrl).isEqualTo(newPokemon.imageUrl)
        }

    @Test
    fun `Given pokemons inserted, when getting all pokemons, then returns the pokemons inserted`() =
        runTest {
            val pokemon = PokemonEntity(1, "Pokemon 1", "http://test-url.com/1")
            val pokemon2 = PokemonEntity(2, "Pokemon 2", "http://test-url.com/2")
            val pokemon3 = PokemonEntity(3, "Pokemon 3", "http://test-url.com/3")
            database.pokemonDao().insertPokemon(pokemon, pokemon2, pokemon3)

            val pokemonsLoaded = database.pokemonDao().observePokemons().first()

            assertThat(pokemonsLoaded.size).isEqualTo(3)
            assertThat(pokemonsLoaded).contains(pokemon)
            assertThat(pokemonsLoaded).contains(pokemon2)
            assertThat(pokemonsLoaded).contains(pokemon3)
        }
}