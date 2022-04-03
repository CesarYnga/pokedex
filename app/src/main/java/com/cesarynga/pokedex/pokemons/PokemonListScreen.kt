package com.cesarynga.pokedex.pokemons

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.ui.theme.PokedexTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun PokemonList(navController: NavController, viewModel: PokemonListViewModel = getViewModel()) {
    PokedexTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
            topBar = {
                TopAppBar() {

                }
            },
        ) {
            Surface(color = MaterialTheme.colors.background) {
                when (val uiState = viewModel.uiState.collectAsState().value) {
                    is PokemonListUiState.Success -> {
                        PokemonList(navController = navController, uiState.pokemonList)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonList(navController: NavController, pokemonList: List<Pokemon>) {
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 180.dp)) {
        items(pokemonList) { pokemon ->
            PokemonListItem(pokemon = pokemon, navController = navController)
        }
    }
//    LazyColumn {
//        items(pokemonList) { pokemon ->
//            PokemonListItem(pokemon = pokemon, navController = navController)
//        }
//    }
}

@Composable
fun PokemonListItem(pokemon: Pokemon, navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .clickable {
            navController.currentBackStackEntry?.savedStateHandle?.set("pokemon", pokemon)
            navController.navigate("pokemonDetails")
        }) {
        AsyncImage(model = pokemon.imageUrl, contentDescription = pokemon.name)
        Text(text = pokemon.name, modifier = Modifier.align(Alignment.CenterHorizontally))
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