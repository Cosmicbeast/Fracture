package com.paradoxlog.app.domain.model

@JvmInline
value class GameFlag(val value: String) {
    init {
        require(value.isNotBlank()) { "Flag identifier must not be blank" }
    }
}
