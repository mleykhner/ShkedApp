package ru.mleykhner.shkedapp.di

import io.realm.kotlin.Realm
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.mleykhner.shkedapp.data.remote.AuthService
import ru.mleykhner.shkedapp.utils.realmConfig

val commonModule = module {
    singleOf(::AuthService)
    single { Realm.open(realmConfig) }
}