package ru.mleykhner.shkedapp.di

import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(appModule())
    }
}