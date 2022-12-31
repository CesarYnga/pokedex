package com.cesarynga.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cesarynga.pokedex.pokemondetail.PokemonDetailScreen
import com.cesarynga.pokedex.pokemons.PokemonListScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppScreen.PokemonListScreen.ROUTE
) {


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppScreen.PokemonListScreen.ROUTE) {
            PokemonListScreen(
                onPokemonClick = { pokemon ->
                    navController.navigate(
                        "${AppScreen.PokemonDetailScreen.name}/${pokemon.id}"
                    )
                }
            )
        }
        composable(
            AppScreen.PokemonDetailScreen.ROUTE,
            arguments = listOf(
                navArgument(AppScreen.PokemonDetailScreen.Args.POKEMON_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.let {
                PokemonDetailScreen()
            }
        }
    }
}