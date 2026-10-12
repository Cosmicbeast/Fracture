package org.example.project.core

import kotlinx.serialization.Serializable

@Serializable
data class Ending(
    val id: String,
    val title: String,
    val summary: String,
    val finalReflection: String,
    val minTyrantIndex: Int = 0,
    val maxTyrantIndex: Int = 100,
    val minStability: Int = 0,
    val maxStability: Int = 100
)
