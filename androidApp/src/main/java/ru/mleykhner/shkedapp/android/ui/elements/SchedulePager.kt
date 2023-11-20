package ru.mleykhner.shkedapp.android.ui.elements

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import ru.mleykhner.shkedapp.vm.ScheduleScreenViewModel
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SchedulePager(
    modifier: Modifier = Modifier,
    viewModel: ScheduleScreenViewModel
) {

    val density = LocalDensity.current
    var widthDp by remember {
        mutableStateOf(0.dp)
    }

    var selectedDate by remember {
        mutableStateOf(viewModel.selectedDate)
    }

    val schedule by viewModel.schedule.collectAsState()

    val anchors = with (density) {
        DraggableAnchors {
            -1f at -(widthDp.toPx())
            0f at 0f
            1f at widthDp.toPx()
        }
    }

    val state = remember { AnchoredDraggableState(
        initialValue = 0f,
        positionalThreshold = { totalDistance: Float -> totalDistance * 0.5f },
        velocityThreshold = { with(density) { 125.dp.toPx() } },
        animationSpec = tween()
    ) }

    viewModel.actions.observeAsActions { action ->
        if (action != ScheduleScreenViewModel.Action.DateChanged) return@observeAsActions

        selectedDate = viewModel.selectedDate
    }

    SideEffect {
        state.updateAnchors(anchors)
    }

    LaunchedEffect(state.currentValue) {
        snapshotFlow { state.currentValue }.collect { value ->
            if (value != 0f) {
                viewModel.selectedDate = selectedDate.plus(value.sign.toInt() * -1, DateTimeUnit.DAY)
            }
            state.snapTo(0f)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal
            )
            .onSizeChanged { size ->
                widthDp = with(density) {
                    size.width.toDp()
                }
            }
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .toInt(),
                        y = 0
                    )
                }
                .requiredWidth(IntrinsicSize.Min)
                .height(maxHeight)
        ) {
            for (delta in -1..1)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = modifier.width(widthDp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    schedule[selectedDate.plus(delta, DateTimeUnit.DAY)]?.let { day ->
                        if (day.isNotEmpty())
                            items(day.size) { index ->
                                LessonCard(
                                    day[index],
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        else {
                            item {
                                Text(
                                    text = "Выходной",
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                    } ?: run {
                        item { CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) }
                    }
            }
        }
    }
}