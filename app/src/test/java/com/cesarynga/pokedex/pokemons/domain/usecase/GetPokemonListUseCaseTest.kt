package com.cesarynga.pokedex.pokemons.domain.usecase

import com.cesarynga.pokedex.MainCoroutineRule
import com.cesarynga.pokedex.data.FakePokemonRepository
import com.cesarynga.pokedex.data.source.PokemonModel
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
class GetPokemonListUseCaseTest {

    private lateinit var getPokemonListUseCase: GetPokemonListUseCase
    private lateinit var fakePokemonRepository: FakePokemonRepository
    private val pokemon1 = PokemonModel(1, "pokemon1", "https://pokeapi.co/api/v2/pokemon/1/")
    private val pokemon2 = PokemonModel(2, "pokemon2", "https://pokeapi.co/api/v2/pokemon/2/")
    private val pokemon3 = PokemonModel(3, "pokemon3", "https://pokeapi.co/api/v2/pokemon/3/")
    private val pokemonModelList = listOf(pokemon1, pokemon2, pokemon3)

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        fakePokemonRepository = FakePokemonRepository(pokemonModelList, false)
        getPokemonListUseCase = GetPokemonListUseCase(fakePokemonRepository)
    }

    @Test
    fun `Given a non empty pokemon repository, when calling pokemon list use case, then the result is mapping correctly`() =
        runTest {
            val pokemonPage = getPokemonListUseCase(20).first()

            assertThat(pokemonPage).isNotNull()
            assertThat(pokemonPage.results.size).isEqualTo(pokemonModelList.size)
            pokemonPage.results.forEachIndexed { index, pokemon ->
                val mapped = pokemonModelList[index].toPokemon()
                assertThat(pokemon).isEqualTo(mapped)
            }
        }

    @Test
    fun `Given a empty pokemon repository, when calling pokemon list use case, then the result empty`() =
        runTest {
            fakePokemonRepository.pokemonList = emptyList()

            val pokemonList = getPokemonListUseCase(20).first()

            assertThat(pokemonList).isNotNull()
            assertThat(pokemonList.results.isEmpty()).isTrue()
        }

    @Test
    fun `Given a unavailable repository, when calling pokemon list use case, then an error is thrown`() =
        runTest {
            fakePokemonRepository.pokemonList = null

            getPokemonListUseCase(20).catch {
                // Result should be an exception
                assertThat(it).isInstanceOf(Exception::class.java)
                assertThat(it.message).isEqualTo("Pokemons not found")
            }.collect()
        }
}