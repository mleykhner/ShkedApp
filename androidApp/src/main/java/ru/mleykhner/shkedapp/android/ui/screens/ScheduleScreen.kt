package ru.mleykhner.shkedapp.android.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.DelicateCoroutinesApi
import ru.mleykhner.shkedapp.android.ui.elements.SchedulePager
import ru.mleykhner.shkedapp.android.ui.elements.TimelineControls
import ru.mleykhner.shkedapp.data.models.LessonViewData
import ru.mleykhner.shkedapp.vm.ScheduleScreenViewModel

@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel = viewModel()
) {
//    val scheduleService = remember {
//        ScheduleServiceImpl()
//    }
//    val date = remember {
//        LocalDate(2023, 10, 6)
//    }
    var schedule by remember {
        mutableStateOf(emptyList<LessonViewData>())
    }
    val refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { /*TODO*/ })

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.pullRefresh(refreshState)
    ) {
//        LazyVerticalGrid(
//            modifier = Modifier.fillMaxSize(),
//            columns = GridCells.Fixed(1),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(schedule) {
//                LessonCard(
//                    lesson = it,
//                    modifier = Modifier.padding(horizontal = 12.dp)
//                )
//            }
//        }
        SchedulePager()
        TimelineControls(viewModel)
    }

}