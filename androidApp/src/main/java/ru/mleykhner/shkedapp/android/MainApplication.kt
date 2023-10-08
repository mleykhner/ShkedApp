package ru.mleykhner.shkedapp.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.mleykhner.shkedapp.di.appModule
import ru.mleykhner.shkedapp.utils.startNapier

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startNapier()
        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(appModule())
        }
    }
}