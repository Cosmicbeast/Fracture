package org.example.project.save

interface StorageBackend {
    fun getString(key: String): String?
    fun setString(key: String, value: String)
    fun remove(key: String)
}

expect fun getPlatformStorage(): StorageBackend
