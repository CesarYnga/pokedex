package com.cesarynga.pokedex.pokemondetail.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPokemonUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke(pokemonId: Int): Flow<Pokemon> =
        pokemonRepository.getPokemon(pokemonId).map {
            it.toPokemon()
        }
}