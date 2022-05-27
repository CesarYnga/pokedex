package com.cesarynga.pokedex.pokemons.domain.model

import android.graphics.Color
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    var cardBackgroundColor: Int = Color.TRANSPARENT,
    var cardContentColor: Int = Color.BLACK,
) : Parcelable