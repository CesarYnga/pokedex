package com.cesarynga.pokedex.pokemondetail.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class GetPokemonStreamUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke(pokemonId: Int) = pokemonRepository.getPokemonStream(pokemonId)
        .onEach {
            if (it.types.isEmpty()) {
                pokemonRepository.getPokemon(pokemonId)
            }
        }
        .map {
            it.toPokemon()
        }
}