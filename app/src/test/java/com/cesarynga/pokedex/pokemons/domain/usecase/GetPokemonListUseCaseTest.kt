package com.cesarynga.pokedex.pokemons.domain.usecase

import com.cesarynga.pokedex.data.FakePokemonRepository
import com.cesarynga.pokedex.data.source.remote.PokemonEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class GetPokemonListUseCaseTest {

    private lateinit var getPokemonListUseCase: GetPokemonListUseCase
    private lateinit var fakePokemonRepository: FakePokemonRepository
    private val pokemon1 = PokemonEntity("pokemon1", "https://pokeapi.co/api/v2/pokemon/1/")
    private val pokemon2 = PokemonEntity("pokemon2", "https://pokeapi.co/api/v2/pokemon/2/")
    private val pokemon3 = PokemonEntity("pokemon3", "https://pokeapi.co/api/v2/pokemon/3/")
    private val pokemonEntityList = listOf(pokemon1, pokemon2, pokemon3)

    @Before
    fun setUp() {
        fakePokemonRepository = FakePokemonRepository(pokemonEntityList)
        getPokemonListUseCase = GetPokemonListUseCase(fakePokemonRepository)
    }

    @Test
    fun `Given a non empty pokemon repository, when calling pokemon list use case, then the result is mapping correctly`() = runBlocking {
        val pokemonList = getPokemonListUseCase(20).first()

        assertThat(pokemonList).isNotNull()
        assertThat(pokemonList.isNotEmpty()).isTrue()
        assertThat(pokemonList.size).isEqualTo(pokemonEntityList.size)
        pokemonList.forEachIndexed { index, pokemon ->
            val mapped = pokemonEntityList[index].toPokemon()
            assertThat(pokemon).isEqualTo(mapped)
        }
    }

    @Test
    fun `Given a empty pokemon repository, when calling pokemon list use case, then the result empty`() = runBlocking {
        fakePokemonRepository.pokemonList = emptyList()

        val pokemonList = getPokemonListUseCase(20).first()

        assertThat(pokemonList).isNotNull()
        assertThat(pokemonList.isEmpty()).isTrue()
    }

    @Test
    fun `Given a unavailable repository, when calling pokemon list use case, then an error is thrown`() = runBlocking {
        fakePokemonRepository.pokemonList = null

        getPokemonListUseCase(20).catch {
            // Result should be an exception
            assertThat(it).isInstanceOf(Exception::class.java)
            assertThat(it.message).isEqualTo("Pokemons not found")
        }.collect()
    }
}