package ru.mleykhner.shkedapp.android.ui.elements


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.until
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.android.ui.theme.weekdaysStyle
import ru.mleykhner.shkedapp.vm.ScheduleScreenViewModel
import java.text.DateFormatSymbols
import java.util.Collections

private const val DATE_SIZE = 46f
private const val GAP_WIDTH = 12f

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: ScheduleScreenViewModel,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    visibleMonth: Month,
    onVisibleMonthChange: (Month) -> Unit
) {

    val firstDayOfWeek = java.util.Calendar.getInstance().firstDayOfWeek
    val density = LocalDensity.current
    val weekdaysNames = remember {
        getShortWeekdaysSymbols()
    }
    
    var widthDp by remember {
        mutableStateOf(0.dp)
    }
    var daysOnScreen by remember {
        mutableIntStateOf(0)
    }
    var dragOffset by remember {
        mutableFloatStateOf(0f)
    }
    var viewOffset by remember {
        mutableStateOf(0.dp)
    }
    var dateOffset by remember {
        mutableIntStateOf(0)
    }
    var generalOffset by remember {
        mutableIntStateOf(0)
    }
    var closestFirstDayOfWeekOffset by remember {
        mutableFloatStateOf(0f)
    }

    val scrollableState = rememberScrollableState { delta ->
        dragOffset += delta
        delta
    }

    val coroutine = rememberCoroutineScope()

    viewModel.dateChange.observeAsActions { newDate ->
        val secondVisibleDay = viewModel.initialDate.plus(1 - dateOffset, DateTimeUnit.DAY)
        val secondToLastVisibleDay = secondVisibleDay.plus(daysOnScreen - 3, DateTimeUnit.DAY)

        if (newDate < secondVisibleDay) {
            val delta = newDate.until(secondVisibleDay, DateTimeUnit.DAY) - 1
            val offset = delta * (DATE_SIZE + GAP_WIDTH)

            with (coroutine) {
                launch {
                    with(density) {
                        scrollableState.animateScrollBy(offset.dp.toPx())
                    }
                }
            }
        } else if (newDate > secondToLastVisibleDay) {
            val delta = newDate.until(secondToLastVisibleDay, DateTimeUnit.DAY) - 1
            val offset = delta * (DATE_SIZE + GAP_WIDTH)

            with (coroutine) {
                launch {
                    with(density) {
                        scrollableState.animateScrollBy(offset.dp.toPx())
                    }
                }
            }
        }
    }

    LaunchedEffect(widthDp) {
        daysOnScreen = (widthDp / (DATE_SIZE + GAP_WIDTH).dp).toInt() + 1
        generalOffset = (((DATE_SIZE + GAP_WIDTH) * daysOnScreen - GAP_WIDTH - widthDp.value) / 2f).toInt()
    }

    LaunchedEffect(dragOffset) {
        viewOffset = (with(density) { dragOffset.toDp() }.value % (DATE_SIZE + GAP_WIDTH)).dp
        dateOffset = (with(density) { dragOffset.toDp() }.value / (DATE_SIZE + GAP_WIDTH)).toInt()
    }

    LaunchedEffect(dateOffset) {
        var hasVisibleMonth = false
        var newMonth = visibleMonth
        for (it in 0..daysOnScreen) {
            val id = it - dateOffset
            val date = viewModel.initialDate.plus(id, unit = DateTimeUnit.DAY)
            if (date.dayOfWeek.value == firstDayOfWeek) {
                closestFirstDayOfWeekOffset = with(density) {
                    (viewModel.initialDate.until(date, DateTimeUnit.DAY) * (DATE_SIZE + GAP_WIDTH)).dp.toPx()
                }
            }
            if (date.month == visibleMonth) {
                hasVisibleMonth = true
            } else {
                newMonth = date.month
            }
        }
        if (!hasVisibleMonth) {
            onVisibleMonthChange(newMonth)
        }
    }

    Box (
        modifier = modifier
            .scrollable(
                orientation = Orientation.Horizontal,
                state = scrollableState
            )
            .onSizeChanged { size ->
                widthDp = with(density) {
                    size.width.toDp()
                }
            }
            .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(GAP_WIDTH.dp),
            modifier = Modifier
                .offset(x = viewOffset)
                .offset(x = generalOffset.dp)
                .offset(x = (-10).dp)
                .requiredWidth(IntrinsicSize.Min)

        ) {
            (-1 until daysOnScreen).forEach {
                val id = it - dateOffset
                val date = viewModel.initialDate.plus(id, unit = DateTimeUnit.DAY)
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(DATE_SIZE.dp)
                            .clip(CircleShape)
                            .clickable {
                                onDateChange(date)
                                onVisibleMonthChange(date.month)
                            }

                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = if (date.month == visibleMonth)
                                MaterialTheme.colorScheme.onSurface else
                                MaterialTheme.colorScheme.secondary
                        )
                        if (date == selectedDate) {
                            Box(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        brush = SolidColor(
                                            MaterialTheme.colorScheme.onSurface
                                        ),
                                        shape = CircleShape
                                    )
                                    .fillMaxSize()
                            )
                        }
                    }
                    Text(
                        text = weekdaysNames[date.dayOfWeek.value - 1].uppercase(),
                        style = weekdaysStyle,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Preview(
    widthDp = 370,
    showBackground = true
)
@Composable
fun Timeline_Preview() {
    val initialDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    var selectedDate by remember {
        mutableStateOf(initialDate)
    }
    val viewModel = remember {
        ScheduleScreenViewModel()
    }
    var visibleMonth by remember {
        mutableStateOf(selectedDate.month)
    }
    AppTheme {
        Surface {
            Timeline(
                modifier = Modifier,
                viewModel = viewModel,
                selectedDate,
                { selectedDate = it },
                visibleMonth,
                { visibleMonth = it })
        }

    }
}

fun isDayOnScreen(
    givenDate: LocalDate,
    initialDate: LocalDate,
    daysOnScreen: Int,
    dateOffset: Int
): Boolean {
    for (i in 0..daysOnScreen) {
        val id = i - dateOffset
        val date = initialDate.plus(id, unit = DateTimeUnit.DAY)
        if (date == givenDate) return true
    }
    return false
}

fun getShortWeekdaysSymbols(): List<String> {
    val formatter = DateFormatSymbols.getInstance()
    val names = formatter.shortWeekdays.drop(1).toList()
    Collections.rotate(names, -1)
    return names
}