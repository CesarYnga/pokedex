package com.cesarynga.pokedex.pokemons

import com.cesarynga.pokedex.MainCoroutineRule
import com.cesarynga.pokedex.data.FakePokemonRepository
import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.pokemons.domain.usecase.GetPokemonListUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonListViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var pokemonListViewModel: PokemonListViewModel
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase
    private lateinit var fakePokemonRepository: FakePokemonRepository
    private val pokemon1 = PokemonModel(1, "pokemon1", "https://pokeapi.co/api/v2/pokemon/1/")
    private val pokemon2 = PokemonModel(2, "pokemon2", "https://pokeapi.co/api/v2/pokemon/2/")
    private val pokemon3 = PokemonModel(3, "pokemon3", "https://pokeapi.co/api/v2/pokemon/3/")
    private val pokemonList = listOf(pokemon1, pokemon2, pokemon3)

    @Before
    fun setUp() {
        fakePokemonRepository = FakePokemonRepository(pokemonList, false)
        getPokemonListUseCase = GetPokemonListUseCase(fakePokemonRepository)
        pokemonListViewModel = PokemonListViewModel(getPokemonListUseCase)
    }

    @Test
    fun `Given a non empty pokemon repository, when getting pokemon list, then loading state is emitted and pokemon list is emitted`() =
        runTest {
            val loading = pokemonListViewModel.uiState.first()
            val success = pokemonListViewModel.uiState.drop(1).first()

            assertThat(loading.isLoading).isTrue()

            assertThat(success.isLoading).isFalse()
            assertThat(success.items).isEqualTo(pokemonList.map { it.toPokemon() })
        }

    @Test
    fun `Given a failure in pokemon repository, when getting pokemon list, then loading state is emitted and error is emitted`() =
        runTest {
            // Make repository unavailable
            fakePokemonRepository.pokemonList = null

            val loading = pokemonListViewModel.uiState.first()
            val error = pokemonListViewModel.uiState.drop(1).first()

            assertThat(loading.isLoading).isTrue()

            assertThat(error.isLoading).isFalse()
            assertThat(error.items).isEmpty()
            assertThat(error.userMessage).isNotEmpty()
        }
}