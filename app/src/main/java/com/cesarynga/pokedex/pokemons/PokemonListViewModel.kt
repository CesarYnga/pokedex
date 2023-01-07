package com.cesarynga.pokedex.pokemons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesarynga.pokedex.R
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.pokemons.domain.usecase.GetPokemonListUseCase
import com.cesarynga.pokedex.pokemons.domain.usecase.ObserveLocalPokemosUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class PokemonListViewModel(
    observeLocalPokemosUseCase: ObserveLocalPokemosUseCase,
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {

    private val pokemons = observeLocalPokemosUseCase().onEach { localPokemons ->
        localPokemons.ifEmpty {
            // Get Pokemons from network
            getPokemonPage(0)
        }
    }

    private val isLoading = MutableStateFlow(false)
    private val isLastPage = MutableStateFlow(false)
    private val errorMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _uiState = MutableStateFlow(PokemonListUiState(isLoading = true))
    val uiState: StateFlow<PokemonListUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            // Combines the latest value from each of the flows, allowing us to generate a
            // view state instance which only contains the latest values.
            combine(isLoading, isLastPage, pokemons) { isLoading, isLastPage, pokemons ->
                PokemonListUiState(
                    pokemons = pokemons,
                    isLoading = isLoading,
                    isLastPage = isLastPage,
                    errorMessage = null
                )
            }.catch { throwable ->
                Timber.e(throwable, "Error in a state flow")
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun getNextPokemonPage() {
        getPokemonPage(uiState.value.pokemons.size)
    }

    private fun getPokemonPage(offset: Int) {
        viewModelScope.launch {
            getPokemonListUseCase(offset)
                .onStart { isLoading.value = true }
                .catch { throwable ->
                    Timber.e(throwable, "Error while loading pokemon page with offset $offset")
                    isLoading.value = false
                    errorMessage.value = R.string.loading_pokemons_error
                }
                .collect {
                    isLoading.value = false
                    isLastPage.value = !it.hasNext
                }
        }
    }
}

data class PokemonListUiState(
    val pokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val isLastPage: Boolean = false,
    val errorMessage: Int? = null
)