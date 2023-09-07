package ru.mleykhner.shkedapp.utils

import io.realm.kotlin.RealmConfiguration
import ru.mleykhner.shkedapp.data.local.UserRealm

val realmConfig = RealmConfiguration.create(
    schema = setOf(
        UserRealm::class
    )
)