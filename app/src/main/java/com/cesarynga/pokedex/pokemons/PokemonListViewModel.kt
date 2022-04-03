package com.cesarynga.pokedex.pokemons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.pokemons.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PokemonListViewModel(private val getPokemonListUseCase: GetPokemonListUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Success(emptyList()))
    val uiState: StateFlow<PokemonListUiState> = _uiState

    init {
        getPokemonList(1)
    }

    private fun getPokemonList(page: Int) {
        viewModelScope.launch {
            getPokemonListUseCase(page)
                .onStart { _uiState.value = PokemonListUiState.Loading }
                .catch { PokemonListUiState.Error(it) }
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