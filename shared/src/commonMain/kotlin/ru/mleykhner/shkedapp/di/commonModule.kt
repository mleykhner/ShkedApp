package ru.mleykhner.shkedapp.di

import io.realm.kotlin.Realm
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.mleykhner.shkedapp.data.remote.AuthService
import ru.mleykhner.shkedapp.data.remote.AuthServiceImpl
import ru.mleykhner.shkedapp.data.remote.PreferencesService
import ru.mleykhner.shkedapp.data.remote.PreferencesServiceImpl
import ru.mleykhner.shkedapp.utils.realmConfig

val commonModule = module {
    singleOf<AuthService>(::AuthServiceImpl)
    singleOf<PreferencesService>(::PreferencesServiceImpl)
    single { Realm.open(realmConfig) }
    //single { logging() }
}