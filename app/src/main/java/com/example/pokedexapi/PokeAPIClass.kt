package com.example.pokedexapi

data class PokemonResponse(
    val name: String,
    val sprites: Sprites,
    val stats: List<Stat>
)

data class Sprites(
    val front_default: String
)

data class Stat(
    val base_stat: Int,
    val stat: StatName
)

data class StatName(
    val name: String
)
