package com.cesarynga.pokedex.pokemondetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PokemonDetails(navController: NavController, pokemonId: Int, pokemonName: String, pokemonImageUrl: String) {
    Column {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemonImageUrl)
                .build(),
            contentDescription = pokemonName,
        )
        Text(text = "Details: ${pokemonName}")
    }
}