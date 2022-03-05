package com.cesarynga.pokedex.pokemons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cesarynga.pokedex.ui.theme.PokedexTheme

@Composable
fun PokemonList(navController: NavController) {
    PokedexTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
            topBar = {
                TopAppBar() {

                }
            },
        ) {
            Surface(color = MaterialTheme.colors.background) {
                Greeting(navController = navController)
            }
        }
    }
}

@Composable
fun PokemonDetails(navController: NavController) {
    Text(text = "Details")
}

@Composable
fun Greeting(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(modifier = Modifier.align(Alignment.Center), onClick = {
            navController.navigate("pokemonDetails")
        }) {
            Text(text = "Go to details")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokedexTheme {
        Greeting(navController = rememberNavController())
    }
}