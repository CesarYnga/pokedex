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

    fun getNextPokemonPage() {
        getPokemonPage(uiState.value.pokemons.size)
    }

    private fun getPokemonPage(offset: Int) {
        viewModelScope.launch {
            getPokemonListUseCase(offset)
                .onStart { emitUiState(items = uiState.value.pokemons, isLoading = true) }
                .catch {
                    emitUiState(items = uiState.value.pokemons, userMessage = it.localizedMessage, isLoading = false)
                }
                .collect {
                    emitUiState(items = uiState.value.pokemons + it.results, isLoading = false, isLastPage = !it.hasNext)
                }
        }
    }

    private fun emitUiState(
        items: List<Pokemon> = emptyList(),
        isLoading: Boolean = false,
        isLastPage: Boolean = false,
        userMessage: String? = null
    ) {
        val uiState = PokemonListUiState(items, isLoading, isLastPage, userMessage)
        this._uiState.value = uiState
    }
}

data class PokemonListUiState(
    val pokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val isLastPage: Boolean = false,
    val userMessage: String? = null
)