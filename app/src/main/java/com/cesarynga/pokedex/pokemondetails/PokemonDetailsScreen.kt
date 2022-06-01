package com.cesarynga.pokedex.pokemondetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon

@Composable
fun PokemonDetails(navController: NavController, pokemon: Pokemon) {
    Column {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.imageUrl)
                .build(),
            contentDescription = pokemon.name,
        )
        Text(text = "Details: ${pokemon.name}")
    }
}