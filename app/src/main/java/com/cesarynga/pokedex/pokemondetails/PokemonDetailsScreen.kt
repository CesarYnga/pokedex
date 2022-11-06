package com.cesarynga.pokedex.pokemondetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cesarynga.pokedex.pokemons.PokemonListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PokemonDetails(
    pokemonId: Int,
    viewModel: PokemonListViewModel = getViewModel()
) {
    Column {
        Text(text = "Details: $pokemonId")
    }
}