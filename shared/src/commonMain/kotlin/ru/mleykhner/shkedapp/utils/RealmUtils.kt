package ru.mleykhner.shkedapp.utils

import io.realm.kotlin.RealmConfiguration
import ru.mleykhner.shkedapp.data.local.models.DayScheduleRealm
import ru.mleykhner.shkedapp.data.local.models.LessonRealm
import ru.mleykhner.shkedapp.data.local.models.ScheduleRealm
import ru.mleykhner.shkedapp.data.local.models.UserRealm
import ru.mleykhner.shkedapp.data.local.models.WeekdayRealm

val realmConfig: RealmConfiguration
    get() {
        return RealmConfiguration.create(
            schema = setOf(
                UserRealm::class,
                ScheduleRealm::class,
                DayScheduleRealm::class,
                WeekdayRealm::class,
                LessonRealm::class
        )
    )
}