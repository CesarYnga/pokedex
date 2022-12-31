package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.MainCoroutineRule
import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.data.source.local.FakePokemonLocalDataSource
import com.cesarynga.pokedex.data.source.remote.FakePokemonRemoteDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonRepositoryTest {

    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var pokemonLocalDataSource: FakePokemonLocalDataSource
    private lateinit var pokemonRemoteDataSource: FakePokemonRemoteDataSource
    private val pokemon1 = PokemonModel(1, "pokemon1", "http://test-url.com/1")
    private val pokemon2 = PokemonModel(2, "pokemon2", "http://test-url.com/2")
    private val pokemon3 = PokemonModel(3, "pokemon3", "http://test-url.com/3")
    private val pokemon4 = PokemonModel(4, "pokemon4", "http://test-url.com/4")
    private val pokemon5 = PokemonModel(5, "pokemon5", "http://test-url.com/5")
    private val pokemon6 = PokemonModel(6, "pokemon6", "http://test-url.com/6")
    private val localPokemons = listOf(pokemon1, pokemon2, pokemon3)
    private val remotePokemons = listOf(pokemon4, pokemon5, pokemon6)

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        pokemonRemoteDataSource = FakePokemonRemoteDataSource(remotePokemons, false)
        pokemonLocalDataSource = FakePokemonLocalDataSource(localPokemons.toMutableList())
        pokemonRepository = PokemonRepositoryImpl(pokemonRemoteDataSource, pokemonLocalDataSource)
    }

    @Test
    fun `Given local data source with no data, when pokemon list is requested, then repository should return remote pokemon list`() =
        runTest {
            val pokemons = pokemonRepository.getPokemonList(20).first()

            assertThat(pokemons.results).isEqualTo(remotePokemons)
        }

    @Test
    fun `Given local data source with available data, when pokemon list first page is requested, then repository should return local pokemon list`() =
        runTest {
            val pokemons = pokemonRepository.getPokemonList(0).first()

            assertThat(pokemons.results).isEqualTo(localPokemons)
        }

    @Test
    fun `Given local data source with available data, when pokemon list middle page is requested, then repository should return local pokemon list`() =
        runTest {
            val pokemons = pokemonRepository.getPokemonList(3).first()

            assertThat(pokemons.results).isEqualTo(remotePokemons)
            assertThat(pokemonLocalDataSource.pokemonList).containsAtLeastElementsIn(localPokemons)
        }

    @Test
    fun `Given local data source with no data and remote data source not available, when pokemon list is requested, then an error is thrown`() =
        runTest {
            // Make remote data source unavailable
            pokemonRemoteDataSource.pokemonList = null

            pokemonRepository.getPokemonList(20).catch {
                // Result should be an exception
                assertThat(it).isInstanceOf(Exception::class.java)
                assertThat(it.message).isEqualTo("Pokemons not found")
            }.collect()
        }
}