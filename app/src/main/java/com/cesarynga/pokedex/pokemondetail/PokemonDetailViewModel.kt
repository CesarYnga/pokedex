package com.cesarynga.pokedex.pokemondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesarynga.pokedex.R
import com.cesarynga.pokedex.navigation.AppScreen
import com.cesarynga.pokedex.pokemondetail.domain.usecase.GetPokemonByIdUseCase
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPokemonByIdUseCase: GetPokemonByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState

    val pokemonId: Int = savedStateHandle[AppScreen.PokemonDetailScreen.Args.POKEMON_ID]!!

    init {
        viewModelScope.launch {
            getPokemonByIdUseCase(pokemonId)
                .onStart { emitUiState(isLoading = true) }
                .catch { emitUiState(isLoading = false, userMessage = R.string.loading_pokemon_error) }
                .collect {
                    emitUiState(pokemon = it, isLoading = false)
                }
        }

    }

    private fun emitUiState(
        pokemon: Pokemon? = null,
        isLoading: Boolean = false,
        userMessage: Int? = null
    ) {
        val uiState = PokemonDetailUiState(pokemon, isLoading, userMessage)
        this._uiState.value = uiState
    }
}

data class PokemonDetailUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)