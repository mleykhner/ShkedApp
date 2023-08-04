package ru.mleykhner.shkedapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform