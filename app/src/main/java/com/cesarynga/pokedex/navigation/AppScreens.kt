package com.cesarynga.pokedex.navigation

sealed class AppScreen(val name: String) {
    object PokemonListScreen : AppScreen("pokemonList") {
        val ROUTE = "$name"
    }

    object PokemonDetailScreen : AppScreen("pokemon_details") {
        val ROUTE = "$name/{${Args.POKEMON_ID}}"
        object Args {
            const val POKEMON_ID = "pokemonId"
        }
    }
}