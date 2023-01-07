package com.cesarynga.pokedex.pokemons.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository
import kotlinx.coroutines.flow.map

class ObserveLocalPokemosUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke() = pokemonRepository.observePokemons().map { pokemonModelList ->
        pokemonModelList.map {
            it.toPokemon()
        }
    }
}