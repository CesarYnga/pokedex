package com.cesarynga.pokedex.data.source.remote.api.response

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.cesarynga.pokedex.data.source.PokemonModel

data class PokemonResponse(
    val id: Int,
    val name: String,
    val url: String,
    val types: List<PokemonTypeSlotResponse>?
) {
    fun toPokemonModel(): PokemonModel {
        val id = if(this.id > 0){
            this.id
        } else {
            try {
                url.split("/".toRegex()).dropLast(1).last().toInt()
            } catch (e: Exception) {
                -1
            }
        }
        val imageUrl = if (id == -1) {
            ""
        } else {
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
        }
        return PokemonModel(
            id = id,
            name = name.capitalize(Locale.current),
            imageUrl = imageUrl,
            types = types?.map { typeSlot ->
                typeSlot.toPokemonTypeModel()
            } ?: emptyList()
        )
    }
}
