package com.cesarynga.pokedex.pokemondetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.pokemons.domain.model.PokemonType
import com.cesarynga.pokedex.ui.theme.PokedexTheme
import com.cesarynga.pokedex.ui.theme.PokemonTypeColor
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
fun PokemonDetailHeader(pokemon: Pokemon, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
        PokemonTypesView(pokemonTypes = pokemon.types)
    }
}

@Composable
fun PokemonTypeView(pokemonType: PokemonType, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(
                color = PokemonTypeColor.getColorByTypeId(pokemonType.id),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = LocalContext.current.resources.getIdentifier(
                    "ic_pokemon_type_${pokemonType.name.lowercase()}",
                    "drawable",
                    LocalContext.current.packageName
                )
            ),
            contentDescription = "",
            tint = Color.White
        )
        Spacer(modifier = modifier.size(8.dp))
        Text(
            text = pokemonType.name,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Composable
fun PokemonTypesView(pokemonTypes: List<PokemonType>, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
        pokemonTypes.forEach {
            PokemonTypeView(pokemonType = it)
        }
    }
}

// ========================
// Previews
// ========================
@Preview(showBackground = true)
@Composable
fun PokemonDetailHeaderPreview() {
    PokedexTheme {
        PokemonDetailHeader(Pokemon(1, "Bulbasaur", "http://test-url.com/1", emptyList()))
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonTypeViewPreview() {
    PokedexTheme {
        PokemonTypeView(pokemonType = PokemonType(1, "fire"), modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonTypeViewsPreview() {
    PokedexTheme {
        PokemonTypesView(
            pokemonTypes = listOf(
                PokemonType(1, "grass"),
                PokemonType(2, "poison")
            )
        )
    }
}