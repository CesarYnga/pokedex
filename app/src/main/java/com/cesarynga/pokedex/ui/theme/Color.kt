package com.cesarynga.pokedex.ui.theme

import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

// Pokemon type colors
enum class PokemonTypeColor(val color: Color) {
    Normal(Color(0xFFB3B5B2)),
    Fighting(Color(0xFFEB4971)),
    Flying(Color(0xFFB8A5F2)),
    Poison(Color(0xFFc582d9)),
    Ground(Color(0xFFF78551)),
    Rock(Color(0xFFd4c9a1)),
    Bug(Color(0xFFB5C534)),
    Ghost(Color(0xFF7f8ac9)),
    Steel(Color(0xFF78aab5)),
    Fire(Color(0xFFF4934D)),
    Water(Color(0xFF75b1e5)),
    Grass(Color(0xFF7fca79)),
    Electric(Color(0xFFF9DF78)),
    Psychic(Color(0xFFfb9d9a)),
    Ice(Color(0xFF91d9cd)),
    Dragon(Color(0xFF8656FA)),
    Dark(Color(0xFF7a7981)),
    Fairy(Color(0xFFf1a6eb));

    companion object {
        fun getColorByTypeId(typeId: Int): Color {
            return values()[typeId - 1].color
        }
    }
}
