package com.cesarynga.pokedex.pokemons.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPokemonListUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke(page: Int): Flow<List<Pokemon>> =
        pokemonRepository.getPokemonList(page).map { pokemons ->
            pokemons.map {
                it.toPokemon()
            }
        }
}