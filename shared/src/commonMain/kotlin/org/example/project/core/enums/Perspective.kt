package org.example.project.core.enums

import kotlinx.serialization.Serializable

@Serializable
enum class Perspective(val displayName: String, val philosophy: String) {
    PAST_SELF(
        displayName = "Past Self",
        philosophy = "Idealistic, reactive, seeking to avoid the predetermined outcome."
    ),
    FUTURE_SELF(
        displayName = "Future Self",
        philosophy = "Pragmatic, burdened, exerting control to prevent timeline collapse."
    )
}
