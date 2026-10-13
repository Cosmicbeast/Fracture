package com.paradoxlog.app.domain.model

data class Item(
    val id: String,
    val name: String,
    val description: String = ""
)
