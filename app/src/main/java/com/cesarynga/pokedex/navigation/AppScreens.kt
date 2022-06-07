package com.cesarynga.pokedex.navigation

sealed class AppScreen(val route: String) {
    object PokemonListScreen : AppScreen("pokemonList")
    object PokemonDetailsScreen : AppScreen("pokemon_details")
}