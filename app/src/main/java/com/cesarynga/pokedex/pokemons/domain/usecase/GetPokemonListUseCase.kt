package com.cesarynga.pokedex.pokemons.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository

class GetPokemonListUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke(page: Int) {
        pokemonRepository.getPokemonList(page)
    }
}