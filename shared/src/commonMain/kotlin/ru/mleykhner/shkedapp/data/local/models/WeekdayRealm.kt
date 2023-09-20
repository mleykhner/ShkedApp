package ru.mleykhner.shkedapp.data.local.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList

class WeekdayRealm: EmbeddedRealmObject {
    var daySchedule: RealmList<DayScheduleRealm> = realmListOf()
    var dayNumber: Int = 0
}

