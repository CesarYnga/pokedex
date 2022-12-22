package com.cesarynga.pokedex.pokemondetail.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPokemonByIdUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke(pokemonId: Int): Flow<Pokemon> =
        pokemonRepository.getPokemonById(pokemonId).map {
            it.toPokemon()
        }
}