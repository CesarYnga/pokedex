package com.cesarynga.pokedex.pokemondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesarynga.pokedex.navigation.AppScreen
import com.cesarynga.pokedex.pokemondetail.domain.usecase.GetPokemonStreamUseCase
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class PokemonDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    getPokemonStreamUseCase: GetPokemonStreamUseCase,
) : ViewModel() {

    private val isLoading = MutableStateFlow(false)
    private val errorMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _uiState = MutableStateFlow(PokemonDetailUiState(isLoading = true))
    val uiState: StateFlow<PokemonDetailUiState>
        get() = _uiState

    private val pokemonId: Int = savedStateHandle[AppScreen.PokemonDetailScreen.Args.POKEMON_ID]!!
    private val pokemon = getPokemonStreamUseCase(pokemonId)

    init {
        viewModelScope.launch {
            combine(pokemon, isLoading, errorMessage) { pokemon, isLoading, errorMessage ->
                PokemonDetailUiState(
                    pokemon = pokemon,
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }.catch { throwable ->
                Timber.e(throwable, "Error getting pokemon with id = $pokemonId")
            }.collect {
                _uiState.value = it
            }
        }
    }
}

data class PokemonDetailUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null
)