package org.example.project.util

object Logger {
    private const val TAG = "[Fracture]"

    fun d(message: String) {
        println("$TAG [DEBUG] $message")
    }

    fun i(message: String) {
        println("$TAG [INFO] $message")
    }

    fun w(message: String) {
        println("$TAG [WARN] $message")
    }

    fun e(message: String, throwable: Throwable? = null) {
        println("$TAG [ERROR] $message")
        throwable?.printStackTrace()
    }
}
