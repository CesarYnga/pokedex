package com.cesarynga.pokedex.pokemons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cesarynga.pokedex.R
import com.cesarynga.pokedex.pokemons.domain.model.Pokemon
import com.cesarynga.pokedex.ui.theme.PokedexTheme
import com.cesarynga.pokedex.util.DominantColors
import com.cesarynga.pokedex.util.rememberDominantColorState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun PokemonListScreen(
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = getViewModel()
) {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.primary)
            .systemBarsPadding()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            },
            modifier = modifier.fillMaxSize()
        ) { contentPadding ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            PokemonListContent(
                loading = uiState.isLoading,
                error = !uiState.userMessage.isNullOrEmpty(),
                lastPage = uiState.isLastPage,
                pokemons = uiState.pokemons,
                onPokemonClick = onPokemonClick,
                onBottomReach = { viewModel.getNextPokemonPage() },
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}

@Composable
fun PokemonListContent(
    loading: Boolean,
    error: Boolean,
    lastPage: Boolean,
    pokemons: List<Pokemon>,
    onBottomReach: () -> Unit,
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = loading,
        empty = pokemons.isEmpty(),
        error = error,
        emptyContent = { PokemonListEmptyContent() },
        errorContent = {
            ErrorRetryContent {
                onBottomReach()
            }
        },
        modifier = modifier
    ) {
        PokemonPagingList(
            loading = loading,
            error = error,
            lastPage = lastPage,
            pokemons = pokemons,
            onPokemonClick = onPokemonClick,
            onBottomReach = onBottomReach,
            modifier = modifier
        )
    }
}

@Composable
fun LoadingContent(
    loading: Boolean,
    empty: Boolean,
    error: Boolean,
    emptyContent: @Composable () -> Unit,
    errorContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (empty) {
        if (loading) {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (error) {
            errorContent()
        } else {
            emptyContent()
        }
    } else {
        content()
    }
}

@Composable
fun PokemonPagingList(
    loading: Boolean,
    error: Boolean,
    lastPage: Boolean,
    pokemons: List<Pokemon>,
    onBottomReach: () -> Unit,
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val reachBottom by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val pokemonCount = listState.layoutInfo.totalItemsCount - 1 // subtract the last item for LoadingItem or RetryItem

            lastVisibleIndex == pokemonCount - 1
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
    ) {
        items(pokemons) { pokemon ->
            PokemonItem(pokemon = pokemon, onPokemonClick = onPokemonClick)
        }

        if (!lastPage) {
            item {
                if (error) {
                    RetryItem {
                        onBottomReach()
                    }
                } else {
                    LoadingItem()
                }
            }
        }
    }

    LaunchedEffect(reachBottom) {
        if (reachBottom && !lastPage && !loading) {
            onBottomReach()
        }
    }
}

@Composable
fun PokemonItem(pokemon: Pokemon, onPokemonClick: (Pokemon) -> Unit) {
    val cardColorState = rememberDominantColorState(
        LocalContext.current,
        DominantColors(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface),
        150
    )

    LaunchedEffect(cardColorState.dominantColors) {
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
                .drawBehind {
                    drawRect(cardColorState.dominantColors.color)
                }
                .clickable {
                    onPokemonClick(pokemon)
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
                    color = cardColorState.dominantColors.onColor.copy(0.6f)
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
                .align(Alignment.TopEnd),
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = pokemon.name
        )
    }
}

@Composable
fun LoadingItem() {
    Box(modifier = Modifier.fillMaxWidth()) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = 24.dp)
                .size(16.dp)
                .align(Alignment.Center),
            strokeWidth = 3.dp
        )
    }
}

@Composable
fun RetryItem(onRetryClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            Icons.Outlined.Refresh,
            "Retry",
            modifier = Modifier
                .padding(top = 16.dp)
                .size(24.dp)
                .align(Alignment.Center)
                .clickable {
                    onRetryClick()
                })
    }
}

@Composable
fun PokemonListEmptyContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("No pokemons found")
    }
}

@Composable
fun RetryButton(modifier: Modifier = Modifier, onRetryClick: () -> Unit) {
    Icon(
        Icons.Outlined.Refresh,
        "Retry",
        modifier = modifier
            .clickable {
                onRetryClick()
            })
}

@Composable
fun ErrorRetryContent(modifier: Modifier = Modifier, onRetryClick: () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        RetryButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .size(24.dp)
                .align(Alignment.Center)
        ) {
            onRetryClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonListItemPreview() {
    PokedexTheme {
        PokemonItem(
            pokemon = Pokemon(1, "Pikachu", ""),
            onPokemonClick = { }
        )
    }
}