package com.cesarynga.pokedex.pokemondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.cesarynga.pokedex.navigation.AppScreen
import com.cesarynga.pokedex.pokemondetail.domain.usecase.GetPokemonById
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PokemonDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getPokemonById: GetPokemonById,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState

    val pokemonId: Int = savedStateHandle[AppScreen.PokemonDetailsScreen.Args.POKEMON_ID]!!
}

data class PokemonDetailUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val userMessage: String? = null
)