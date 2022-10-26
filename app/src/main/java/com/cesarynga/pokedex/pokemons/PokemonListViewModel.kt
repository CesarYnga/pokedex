package com.cesarynga.pokedex.pokemons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.pokemons.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PokemonListViewModel(private val getPokemonListUseCase: GetPokemonListUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState

    init {
        getPokemonPage(0)
    }

    fun getPokemonPage(offset: Int) {
        viewModelScope.launch {
            getPokemonListUseCase(offset)
                .onStart { emitUiState(items = uiState.value.items, isLoading = true) }
                .catch {
                    emitUiState(items = uiState.value.items, userMessage = it.localizedMessage, isLoading = false)
                }
                .collect {
                    emitUiState(items = uiState.value.items + it.results, isLoading = false, hasEndReached = !it.hasNext)
                }
        }
    }

    private fun emitUiState(
        items: List<Pokemon> = emptyList(),
        isLoading: Boolean = false,
        hasEndReached: Boolean = false,
        userMessage: String? = null
    ) {
        val uiState = PokemonListUiState(items, isLoading, hasEndReached, userMessage)
        this._uiState.value = uiState
    }
}

data class PokemonListUiState(
    val items: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val hasEndReached: Boolean = false,
    val userMessage: String? = null
)