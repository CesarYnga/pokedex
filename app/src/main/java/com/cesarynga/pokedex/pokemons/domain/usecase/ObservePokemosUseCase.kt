package com.cesarynga.pokedex.pokemons.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository
import com.cesarynga.pokedex.pokemons.domain.model.PokemonPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObservePokemosUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke() = pokemonRepository.observePokemons().map { pokemonModelList ->
        pokemonModelList.map {
            it.toPokemon()
        }
    }
}