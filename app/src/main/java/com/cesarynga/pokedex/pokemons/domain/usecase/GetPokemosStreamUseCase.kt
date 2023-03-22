package com.cesarynga.pokedex.pokemons.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository
import kotlinx.coroutines.flow.map

class GetPokemosStreamUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke() = pokemonRepository.getPokemonsStream().map { pokemonModelList ->
        pokemonModelList.map {
            it.toPokemon()
        }
    }
}