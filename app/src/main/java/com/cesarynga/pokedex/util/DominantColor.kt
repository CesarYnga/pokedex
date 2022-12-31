package com.cesarynga.pokedex.util

import android.content.Context
import androidx.collection.LruCache
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberDominantColorState(
    context: Context,
    dominantColors: DominantColors = DominantColors(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.onSurface
    ),
    cacheSize: Int = 20
): DominantColorState = remember {
    DominantColorState(context, dominantColors, cacheSize)
}

/**
 * A class that holds dominant colors.
 * @param color The dominant color calculated from an image.
 * @param onColor The an appropriate color to use for text which is displayed over [color].
 */
@Immutable
data class DominantColors(val color: Color, val onColor: Color)

/**
 * A class which stores and caches the result of any calculated dominant colors
 * from images.
 *
 * @param context Android context
 * @param defaultDominantColors The default colors, which will be used if [calculateDominantColor] fails to
 * calculate a dominant color
 * @param cacheSize The size of the [LruCache] used to store recent results. Pass `0` to
 * disable the cache.
 */
@Stable
class DominantColorState(
    private val context: Context,
    private val defaultDominantColors: DominantColors,
    private val cacheSize: Int,
) {
    var dominantColors by mutableStateOf(defaultDominantColors)

    private val cache = when {
        cacheSize > 0 -> LruCache<String, DominantColors>(cacheSize)
        else -> null
    }

    /**
     * Fetches the given [imageUrl] with Coil, then uses [Palette] to calculate the dominant color.
     */
    private suspend fun calculateDominantColor(imageUrl: String): DominantColors {
        val cached = cache?.get(imageUrl)
        if (cached != null) {
            // If we already have the result cached, return early now...
            return cached
        }

        return getDominantSwatchInImage(context, imageUrl)?.let { dominantSwatch ->
            val dominantColor = dominantSwatch.rgb
            val onDominantColor = dominantSwatch.titleTextColor
            return DominantColors(
                Color(dominantColor),
                Color(onDominantColor).copy(alpha = 1f)
            ).also { dominantColors ->
                // Cache the resulting [DominantColors]
                cache?.put(imageUrl, dominantColors)
            }
        } ?: defaultDominantColors
    }

    /**
     * Fetches the given [imageUrl] with Coil, then uses [Palette] to calculate the dominant color.
     */
    private suspend fun getDominantSwatchInImage(
        context: Context,
        imageUrl: String
    ): Palette.Swatch? {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            // We scale the image to cover 48px x 48px (i.e. min dimension == 48px)
            .size(48)
            .scale(Scale.FILL)
            // Disable hardware bitmaps, since Palette uses Bitmap.getPixels()
            .allowHardware(false)
            .memoryCacheKey("$imageUrl.palette")
            .build()

        val bitmap = when (val imageResult = context.imageLoader.execute(request)) {
            is SuccessResult -> imageResult.drawable.toBitmap()
            else -> null
        }

        return bitmap?.let {
            withContext(Dispatchers.Default) {
                val palette = Palette.from(it)
                    // Disable any bitmap resizing in Palette. We've already loaded an appropriately
                    // sized bitmap through Coil
                    .resizeBitmapArea(0)
                    // We reduce the maximum color count down to 16
                    .maximumColorCount(16)
                    .generate()
                palette.dominantSwatch
            }
        }
    }

    suspend fun updateDominantColor(imageUrl: String) {
        val calculatedColors = calculateDominantColor(imageUrl)
        dominantColors = calculatedColors
    }
}