package ru.mleykhner.shkedapp.android.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import ru.mleykhner.shkedapp.android.ui.elements.SchedulePager
import ru.mleykhner.shkedapp.android.ui.elements.TimelineControls
import ru.mleykhner.shkedapp.vm.ScheduleScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel = viewModel()
) {

    val isLoading by viewModel.isLoading.collectAsState()
    val refreshState = rememberPullRefreshState(refreshing = isLoading, onRefresh = { viewModel.updateSchedule("М3О-325Бк-21") })

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
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
        SchedulePager(
            Modifier.pullRefresh(refreshState),
            viewModel = viewModel
        )
        TimelineControls(viewModel = viewModel)
        PullRefreshIndicator(refreshing = isLoading, state = refreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }

}