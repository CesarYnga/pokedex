package com.cesarynga.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cesarynga.pokedex.pokemons.PokemonDetails
import com.cesarynga.pokedex.pokemons.PokemonList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "pokemonList") {
                composable("pokemonList") { PokemonList(navController = navController) }
                composable("pokemonDetails") { PokemonDetails(navController = navController) }
            }
        }
    }
}