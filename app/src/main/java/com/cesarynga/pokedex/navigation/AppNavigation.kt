package com.cesarynga.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cesarynga.pokedex.pokemondetails.PokemonDetails
import com.cesarynga.pokedex.pokemons.PokemonList

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreen.PokemonListScreen.route) {
        composable(AppScreen.PokemonListScreen.route) { PokemonList(navController = navController) }
        composable(
            AppScreen.PokemonDetailsScreen.route + "/{pokemonId}" + "/{pokemonName}" + "/{pokemonImageUrl}",
            arguments = listOf(
                navArgument("pokemonId") { type = NavType.IntType },
                navArgument("pokemonName") { type = NavType.StringType },
                navArgument("pokemonImageUrl") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.let {
                PokemonDetails(
                    navController = navController,
                    it.getInt("pokemonId"),
                    it.getString("pokemonName", ""),
                    it.getString("pokemonImageUrl", "")
                )
            }
        }
    }
}