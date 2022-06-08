package com.cesarynga.pokedex.pokemons

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.htmlEncode
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cesarynga.pokedex.R
import com.cesarynga.pokedex.navigation.AppScreen
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.ui.theme.PokedexTheme
import com.cesarynga.pokedex.util.DominantColors
import com.cesarynga.pokedex.util.rememberDominantColorState
import okio.ByteString.Companion.encodeUtf8
import org.koin.androidx.compose.getViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonList(navController: NavController, viewModel: PokemonListViewModel = getViewModel()) {
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
            PokemonList(
                navController = navController,
                modifier = Modifier.padding(contentPadding),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun PokemonList(navController: NavController, modifier: Modifier, viewModel: PokemonListViewModel) {

    val uiState = viewModel.uiState.collectAsState().value

    if (uiState.isLoading && uiState.items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (uiState.items.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = modifier,
        ) {
            itemsIndexed(uiState.items) { index, pokemon ->
                if (index >= uiState.items.size - 1 && !uiState.hasEndReached && !uiState.isLoading) {
                    viewModel.getPokemonNextPage()
                }

                PokemonListItem(pokemon = pokemon, navController = navController)
            }
            if (!uiState.hasEndReached) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }

    if (!uiState.userMessage.isNullOrEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.userMessage,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun PokemonListItem(pokemon: Pokemon, navController: NavController) {
    Log.i("PokemonListItem", "compose ${pokemon.name}")

    val cardColorState = rememberDominantColorState(
        LocalContext.current,
        DominantColors(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)
    )

    LaunchedEffect(cardColorState.dominantColors) {
        Log.d("PokemonListItem", "${pokemon.name} requires dominant color calculation")
        cardColorState.updateDominantColor(pokemon.imageUrl)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(5f / 2f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .padding(top = 32.dp)
                .shadow(8.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(cardColorState.dominantColors.color)
                .clickable {
                    navController.navigate(
                        AppScreen.PokemonDetailsScreen.route + "/${pokemon.id}" + "/${pokemon.name}" + "/${
                            URLEncoder.encode(
                                pokemon.imageUrl,
                                Charsets.UTF_8.toString()
                            )
                        }"
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = String.format("#%03d", pokemon.id),
                    color = cardColorState.dominantColors.onColor.copy(0.6f),
                )
                Text(
                    text = pokemon.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = cardColorState.dominantColors.onColor
                )
            }
        }
        AsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .align(Alignment.TopEnd),
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = pokemon.name
        )
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