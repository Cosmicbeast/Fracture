package org.example.project.util

object TimeUtils {
    fun formatEpochToReadable(timestamp: Long): String {
        return "Cycle #$timestamp"
    }
}
