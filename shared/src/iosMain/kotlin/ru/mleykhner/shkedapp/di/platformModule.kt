package ru.mleykhner.shkedapp.di

import com.liftric.kvault.KVault
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.mleykhner.shkedapp.data.local.FileStorage
import ru.mleykhner.shkedapp.data.remote.httpClient

actual val platformModule = module {
    factoryOf(::FileStorage)
    single { httpClient() }
    factory { KVault() }
}