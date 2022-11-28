package com.cesarynga.pokedex.pokemons

import com.cesarynga.pokedex.MainCoroutineRule
import com.cesarynga.pokedex.data.FakePokemonRepository
import com.cesarynga.pokedex.data.source.PokemonModel
import com.cesarynga.pokedex.pokemons.domain.usecase.GetPokemonListUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonListViewModelTest {

    private lateinit var pokemonListViewModel: PokemonListViewModel
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase
    private lateinit var fakePokemonRepository: FakePokemonRepository
    private val pokemon1 = PokemonModel(1, "pokemon1", "https://pokeapi.com/api/v2/pokemon/1")
    private val pokemon2 = PokemonModel(2, "pokemon2", "https://pokeapi.com/api/v2/pokemon/2")
    private val pokemon3 = PokemonModel(3, "pokemon3", "https://pokeapi.com/api/v2/pokemon/3")
    private val pokemon4 = PokemonModel(4, "pokemon1", "https://pokeapi.com/api/v2/pokemon/4")
    private val pokemon5 = PokemonModel(5, "pokemon2", "https://pokeapi.com/api/v2/pokemon/5")
    private val pokemon6 = PokemonModel(6, "pokemon3", "https://pokeapi.com/api/v2/pokemon/6")
    private val pokemonListPage1 = listOf(pokemon1, pokemon2, pokemon3)
    private val pokemonListPage2 = listOf(pokemon4, pokemon5, pokemon6)

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        fakePokemonRepository = FakePokemonRepository()
        fakePokemonRepository.addPokemons(*pokemonListPage1.toTypedArray())
        getPokemonListUseCase = GetPokemonListUseCase(fakePokemonRepository)
        pokemonListViewModel = PokemonListViewModel(getPokemonListUseCase)
    }

    @Test
    fun `Given an initialized PokemonListViewModel, when getting pokemon first page, then loading state is emitted and pokemon list is emitted`() =
        runTest {
            assertThat(pokemonListViewModel.uiState.first().isLoading).isFalse()
            assertThat(pokemonListViewModel.uiState.first().pokemons.size).isEqualTo(pokemonListPage1.size)
        }

    @Test
    fun `Given an initialized PokemonListViewModel, when getting pokemon second page, then loading state is false and pokemon list returns first page plus second page and error message is returned`() =
        runTest {
            // Load second page
            pokemonListViewModel.getNextPokemonPage()

            assertThat(pokemonListViewModel.uiState.first().isLoading).isFalse()
            assertThat(pokemonListViewModel.uiState.first().pokemons.size).isEqualTo(pokemonListPage1.size + pokemonListPage2.size)
        }

    @Test
    fun `Given a PokemonListViewModel initialized with a failing repository, when getting pokemon first page, then loading state is false and pokemon list is empty and error message is returned`() =
        runTest {
            // Make the repository fail
            fakePokemonRepository.setReturnError(true)
            getPokemonListUseCase = GetPokemonListUseCase(fakePokemonRepository)
            pokemonListViewModel = PokemonListViewModel(getPokemonListUseCase)

            assertThat(pokemonListViewModel.uiState.first().isLoading).isFalse()
            assertThat(pokemonListViewModel.uiState.first().pokemons).isEmpty()
            assertThat(pokemonListViewModel.uiState.first().userMessage).isNotEmpty()
        }

    @Test
    fun `Given an initialized PokemonListViewModel, when getting pokemon second page with a failing repository, then loading state is false and pokemon list returns first page and error message is returned`() =
        runTest {
            // Make the repository fail
            fakePokemonRepository.setReturnError(true)

            // Load second page
            pokemonListViewModel.getNextPokemonPage()

            assertThat(pokemonListViewModel.uiState.first().isLoading).isFalse()
            assertThat(pokemonListViewModel.uiState.first().pokemons.size).isEqualTo(pokemonListPage1.size)
            assertThat(pokemonListViewModel.uiState.first().userMessage).isNotEmpty()
        }
}