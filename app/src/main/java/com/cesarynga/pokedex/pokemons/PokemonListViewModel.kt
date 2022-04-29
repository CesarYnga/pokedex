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

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Success(emptyList()))
    val uiState: StateFlow<PokemonListUiState> = _uiState

    init {
        getPokemonList(1)
    }

    fun getPokemonList(page: Int) {
        viewModelScope.launch {
            getPokemonListUseCase(page)
                .onStart { _uiState.value = PokemonListUiState.Loading }
                .catch { _uiState.value = PokemonListUiState.Error(it) }
                .collect {
                    _uiState.value = PokemonListUiState.Success(it)
                }
        }
    }
}

sealed class PokemonListUiState {
    data class Success(val pokemonList: List<Pokemon>): PokemonListUiState()
    data class Error(val exception: Throwable): PokemonListUiState()
    object Loading: PokemonListUiState()
}