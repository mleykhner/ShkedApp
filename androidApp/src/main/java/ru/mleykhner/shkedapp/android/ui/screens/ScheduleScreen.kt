package ru.mleykhner.shkedapp.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import ru.mleykhner.shkedapp.android.ui.elements.LessonCard
import ru.mleykhner.shkedapp.data.ScheduleService
import ru.mleykhner.shkedapp.data.models.LessonViewData

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ScheduleScreen() {
    val scheduleService = remember {
        ScheduleService()
    }
    val date = remember {
        LocalDate(2023, 10, 6)
    }
    var schedule by remember {
        mutableStateOf(emptyList<LessonViewData>())
    }

    Column {
        Button(onClick = {
            GlobalScope.launch(Dispatchers.IO) {
                scheduleService.update("М3О-325Бк-21")
                schedule = scheduleService.getScheduleForDate(date, "М3О-325Бк-21")
            }
        }) {
            Text(text = "Получить расписание")
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(schedule) {
                LessonCard(
                    lesson = it,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }

}