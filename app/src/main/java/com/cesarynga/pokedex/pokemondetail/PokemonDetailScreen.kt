package com.cesarynga.pokedex.pokemondetail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.cesarynga.pokedex.pokemons.PokemonListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PokemonDetail(
    pokemonId: Int,
    viewModel: PokemonListViewModel = getViewModel()
) {
    Column {
        Text(text = "Details: $pokemonId")
    }
}