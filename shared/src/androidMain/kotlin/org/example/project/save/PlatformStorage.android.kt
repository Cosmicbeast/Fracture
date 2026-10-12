package org.example.project.save

class AndroidStorageBackend : StorageBackend {
    private val memoryStore = mutableMapOf<String, String>()

    override fun getString(key: String): String? = memoryStore[key]

    override fun setString(key: String, value: String) {
        memoryStore[key] = value
    }

    override fun remove(key: String) {
        memoryStore.remove(key)
    }
}

private val singletonBackend = AndroidStorageBackend()

actual fun getPlatformStorage(): StorageBackend = singletonBackend
