package com.cesarynga.pokedex.data

import com.cesarynga.pokedex.data.source.FakePokemonDataSource
import com.cesarynga.pokedex.data.source.remote.PokemonEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class PokemonRepositoryImplTest {

    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var pokemonRemoteDataSource: FakePokemonDataSource
    private val pokemon1 = PokemonEntity("pokemon1", "url1")
    private val pokemon2 = PokemonEntity("pokemon2", "url2")
    private val pokemon3 = PokemonEntity("pokemon3", "url3")
    private val remotePokemons = listOf(pokemon1, pokemon2, pokemon3)

    @Before
    fun setUp() {
        pokemonRemoteDataSource = FakePokemonDataSource(remotePokemons)
        pokemonRepository = PokemonRepositoryImpl(pokemonRemoteDataSource)
    }

    @Test
    fun `Given remote data source is available, when pokemon list is requested, then repository should return remote pokemon list`() =
        runBlocking {

            val pokemons = pokemonRepository.getPokemonList(20).first()

            assertThat(pokemons).isEqualTo(remotePokemons)
        }

    @Test
    fun `Given remote data source is unavailable, when pokemon list is requested, then an error is thrown`() = runBlocking {
        // Make remote data source unavailable
        pokemonRemoteDataSource.pokemonList = null

        pokemonRepository.getPokemonList(20).catch {
            // Result should be an exception
            assertThat(it).isInstanceOf(Exception::class.java)
            assertThat(it.message).isEqualTo("Pokemons not found")
        }.collect()
    }
}