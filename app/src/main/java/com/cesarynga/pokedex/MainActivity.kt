package com.cesarynga.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cesarynga.pokedex.pokemondetails.PokemonDetails
import com.cesarynga.pokedex.pokemons.PokemonList
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "pokemonList") {
                composable("pokemonList") { PokemonList(navController = navController) }
                composable("pokemonDetails") {
                    val pokemon =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Pokemon>("pokemon")
                    pokemon?.let {
                        PokemonDetails(navController = navController, it)
                    }
                }
            }

//            PokedexTheme {
//                Box(
//                    Modifier
//                        .background(MaterialTheme.colors.primary)
//                        .statusBarsPadding()
//                ) {
//                    Box(
//                        Modifier
//                            .background(Color.Red)
//                            .displayCutoutPadding()
//                    ) {
//                        // app content
//                    }
//                }
//            }
        }
    }
}