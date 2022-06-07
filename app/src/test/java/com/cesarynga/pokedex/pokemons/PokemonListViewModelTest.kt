package com.cesarynga.pokedex.pokemons

import com.cesarynga.pokedex.MainCoroutineRule
import com.cesarynga.pokedex.data.FakePokemonRepository
import com.cesarynga.pokedex.data.source.remote.PokemonResponse
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
    private val pokemon1 = PokemonResponse("pokemon1", "https://pokeapi.co/api/v2/pokemon/1/")
    private val pokemon2 = PokemonResponse("pokemon2", "https://pokeapi.co/api/v2/pokemon/2/")
    private val pokemon3 = PokemonResponse("pokemon3", "https://pokeapi.co/api/v2/pokemon/3/")
    private val pokemonEntityList = listOf(pokemon1, pokemon2, pokemon3)

    @Before
    fun setUp() {
        fakePokemonRepository = FakePokemonRepository(pokemonEntityList)
        getPokemonListUseCase = GetPokemonListUseCase(fakePokemonRepository)
        pokemonListViewModel = PokemonListViewModel(getPokemonListUseCase)
    }

    @Test
    fun `Given a non empty pokemon repository, when getting pokemon list, then loading state is emitted and pokemon list is emitted`() = runTest {
        pokemonListViewModel.getPokemonNextPage(1)

        val loading = pokemonListViewModel.uiState.first()
        val success = pokemonListViewModel.uiState.drop(1).first()

        assertThat(loading).isInstanceOf(PokemonListUiState.Loading::class.java)

        assertThat(success).isInstanceOf(PokemonListUiState.Success::class.java)
        assertThat((success as PokemonListUiState.Success).pokemonList).hasSize(3)
    }

    @Test
    fun `Given a failure in pokemon repository, when getting pokemon list, then loading state is emitted and error is emitted`() = runTest {
        // Make remote data source unavailable
        fakePokemonRepository.pokemonList = null

        pokemonListViewModel.getPokemonNextPage(1)

        val loading = pokemonListViewModel.uiState.first()
        val error = pokemonListViewModel.uiState.drop(1).first()

        assertThat(loading).isInstanceOf(PokemonListUiState.Loading::class.java)
        assertThat(error).isInstanceOf(PokemonListUiState.Error::class.java)
    }
}