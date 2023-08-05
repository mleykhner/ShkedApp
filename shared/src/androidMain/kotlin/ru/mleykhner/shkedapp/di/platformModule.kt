package ru.mleykhner.shkedapp.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.mleykhner.shkedapp.data.remote.FileStorage
import ru.mleykhner.shkedapp.data.remote.httpClient

actual val platformModule = module {
    singleOf (::FileStorage)
    single { httpClient() }
}