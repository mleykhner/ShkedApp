package ru.mleykhner.shkedapp.android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import ru.mleykhner.shkedapp.android.ui.elements.LessonCard
import ru.mleykhner.shkedapp.android.ui.elements.TimelineControls
import ru.mleykhner.shkedapp.data.ScheduleServiceImpl
import ru.mleykhner.shkedapp.data.models.LessonViewData

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ScheduleScreen() {
    val scheduleService = remember {
        ScheduleServiceImpl()
    }
    val date = remember {
        LocalDate(2023, 10, 6)
    }
    var schedule by remember {
        mutableStateOf(emptyList<LessonViewData>())
    }
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)

    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = refreshState,
            swipeEnabled = true,
            onRefresh = {
                GlobalScope.launch(Dispatchers.IO) {
                    scheduleService.refresh("М3О-325Бк-21")
                    schedule = scheduleService.getScheduleByDate("М3О-325Бк-21", LocalDate.parse("2023-10-23")) ?: emptyList()
                }
            }
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
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
        TimelineControls()
    }

}