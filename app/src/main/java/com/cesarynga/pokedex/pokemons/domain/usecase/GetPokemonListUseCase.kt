package com.cesarynga.pokedex.pokemons.domain.usecase

import com.cesarynga.pokedex.data.PokemonRepository
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.pokemons.domain.model.PokemonPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPokemonListUseCase(private val pokemonRepository: PokemonRepository) {

    operator fun invoke(page: Int): Flow<PokemonPage> =
        pokemonRepository.getPokemonList(page).map {
            it.toPokemonPage()
        }
}