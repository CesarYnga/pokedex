package com.cesarynga.pokedex.data.source.remote.api.response

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.cesarynga.pokedex.data.source.PokemonTypeModel

data class PokemonTypeSlotResponse(val slot: Int, val type: PokemonTypeResponse) {

    fun toPokemonTypeModel() : PokemonTypeModel {
        val id = try {
            type.url.split("/".toRegex()).dropLast(1).last().toInt()
        } catch (e: Exception) {
            -1
        }
        return PokemonTypeModel(id, type.name.capitalize(Locale.current))
    }
}
