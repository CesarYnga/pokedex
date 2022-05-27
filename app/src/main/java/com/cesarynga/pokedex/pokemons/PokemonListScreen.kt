package com.cesarynga.pokedex.pokemons

import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cesarynga.pokedex.R
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.ui.theme.PokedexTheme
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonList(navController: NavController, viewModel: PokemonListViewModel = getViewModel()) {
    PokedexTheme(darkTheme = false) {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.primary)
                .systemBarsPadding()
        ) {
            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        title = { Text(text = stringResource(id = R.string.app_name)) },
                        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                    )
                },
            ) { contentPadding ->
                when (val uiState = viewModel.uiState.collectAsState().value) {
                    is PokemonListUiState.Success -> {
                        PokemonList(
                            navController = navController,
                            modifier = Modifier.padding(contentPadding),
                            uiState.pokemonList
                        )
                    }
                    is PokemonListUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    is PokemonListUiState.Error -> {
                        Toast.makeText(
                            LocalContext.current,
                            uiState.exception.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonDetails(navController: NavController, pokemon: Pokemon) {
    Text(text = "Details: ${pokemon.name}")
}

@Composable
fun PokemonList(navController: NavController, modifier: Modifier, pokemonList: List<Pokemon>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 176.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
    ) {
        items(pokemonList) { pokemon ->
            PokemonListItem(pokemon = pokemon, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListItem(pokemon: Pokemon, navController: NavController) {
    Log.i("PokemonListItem", "compose ${pokemon.name}")
    var cardBackgroundColor by remember { mutableStateOf(pokemon.cardBackgroundColor) }
    var cardContentColor by remember { mutableStateOf(pokemon.cardContentColor) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color(cardBackgroundColor),
            contentColor = androidx.compose.ui.graphics.Color(cardContentColor)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("pokemon", pokemon)
                navController.navigate("pokemonDetails")
            },
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .allowHardware(false)
                    .data(pokemon.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = pokemon.name,
                onSuccess = {
                    if (pokemon.cardBackgroundColor == Color.TRANSPARENT) {
                        Log.i("PokemonListItem", "image loading success ${pokemon.name}")
                        Palette.from(it.result.drawable.toBitmap())
                            .generate { palette ->
                            val backgroundColor = palette?.dominantSwatch?.rgb
                            if (backgroundColor != null) {
                                pokemon.cardBackgroundColor = backgroundColor
                                cardBackgroundColor = backgroundColor
                                val contentColor = palette.dominantSwatch?.titleTextColor
                                if (contentColor != null) {
                                    pokemon.cardContentColor = contentColor
                                    cardContentColor = contentColor
                                }
                            }
                        }
                    }
                },
            )
            Text(text = pokemon.name, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonListItemPreview() {
    PokedexTheme {
        PokemonListItem(
            pokemon = Pokemon(1, "Pikachu", ""),
            navController = rememberNavController()
        )
    }
}