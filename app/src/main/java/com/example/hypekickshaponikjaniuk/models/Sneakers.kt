package com.example.hypekickshaponikjaniuk.models

import java.io.Serializable

data class Sneakers(
    val brand: String = "",
    val modelName: String = "",
    val releaseYear: Int = 0,
    val resellPrice: Int = 0,
    val imageUrl: String = ""
) : Serializable