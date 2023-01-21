package com.cesarynga.pokedex.pokemondetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.ui.theme.PokedexTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        uiState.pokemon?.let {
            PokemonDetailHeader(pokemon = it)
        }
    }
}

@Composable
fun PokemonDetailHeader(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(5f / 3f)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        AsyncImage(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            model = ImageRequest.Builder(LocalContext.current).data(pokemon.imageUrl)
                .crossfade(true).build(),
            contentDescription = pokemon.name
        )
        Text(text = String.format("#%03d", pokemon.id))
        Text(
            text = pokemon.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ========================
// Previews
// ========================
@Preview(showBackground = true)
@Composable
fun PokemonDetailHeaderPreview() {
    PokedexTheme {
        PokemonDetailHeader(Pokemon(1, "Bulbasaur", "http://test-url.com/1"))
    }
}