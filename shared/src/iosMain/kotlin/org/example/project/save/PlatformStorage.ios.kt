package org.example.project.save

import platform.Foundation.NSUserDefaults

class IosStorageBackend : StorageBackend {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun getString(key: String): String? = defaults.stringForKey(key)

    override fun setString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    override fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }
}

private val singletonBackend = IosStorageBackend()

actual fun getPlatformStorage(): StorageBackend = singletonBackend
