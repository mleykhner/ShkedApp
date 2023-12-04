package ru.mleykhner.shkedapp.android.ui.elements


import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.until
import ru.mleykhner.shkedapp.android.R
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.android.ui.theme.weekdaysStyle
import ru.mleykhner.shkedapp.utils.getMonthLabel
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar() {
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            density = LocalDensity.current,
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )



    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetShadowElevation = 6.dp,
        sheetPeekHeight = 172.dp,
        containerColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            CalendarControls(
                bottomSheetState
                    .bottomSheetState
                    .targetValue
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(text = "test")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMotionApi::class)
@Composable
fun CalendarControls(state: SheetValue) {
    var selectedDate by rememberSaveable {
        mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    }
    val context = LocalContext.current
    val controlsMotionScene = remember {
        context.resources
            .openRawResource(R.raw.calendar_controls_transition)
            .readBytes()
            .decodeToString()
    }
    val weekdaysNames = remember {
        getShortWeekdaysSymbols()
    }
    var monthLabel by remember { mutableStateOf(getMonthLabel(selectedDate.month)) }
    val progress by animateFloatAsState(if (state == SheetValue.Expanded) 1f else 0f, label = "")


    LaunchedEffect(selectedDate) {
        monthLabel = getMonthLabel(selectedDate.month)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        MotionLayout(
            motionScene = MotionScene(content = controlsMotionScene),
            progress = progress,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = monthLabel,
                label = "monthChange",
                transitionSpec = {
                    fadeIn(animationSpec = tween(100, 0)) togetherWith fadeOut()
                },
                modifier = Modifier
                    .layoutId("monthLabel")
                    .animateContentSize()
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.animateContentSize()
                )
            }
            FilledTonalIconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .layoutId("nextButton")
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
            }
            FilledTonalIconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .layoutId("previousButton")
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
            }
        }
        WeekObserver(selectedDate) {
            Log.i("WeekObserver", "Date changed: $it")
            selectedDate = it
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            weekdaysNames.forEach { day ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(46.dp)
                ) {
                    Text(
                        text = day.uppercase(),
                        style = weekdaysStyle,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
//        MotionLayout(motionScene = MotionScene(content = sheetMotionScene), progress = progress) {
//            Box(modifier = Modifier.layoutId("serviceBox"))
//        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeekObserver(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit = {}) {
    val weekPagerState = rememberPagerState(
        pageCount = { 3 },
        initialPage = 1
    )

    var weeks = remember {
        (-1..1).map { getWeek(selectedDate.plus(it, unit = DateTimeUnit.WEEK)) }
    }

    LaunchedEffect(weekPagerState) {
        snapshotFlow { weekPagerState.settledPage }.collect { page ->
            if (page != 1) {
                Log.i("WeekObserver", "Pager state changed: $page")
                onDateChange(selectedDate.plus(page - 1, unit = DateTimeUnit.WEEK))
                weeks = (-1..1).map { getWeek(selectedDate.plus(it, unit = DateTimeUnit.WEEK)) }
                weekPagerState.scrollToPage(1)
            }
        }
    }

    HorizontalPager(weekPagerState) { page ->
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            weeks[page].forEach { date ->
                IconButton(onClick = {onDateChange(date)}) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(46.dp)
                            .height(46.dp)
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (date == selectedDate),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        brush = SolidColor(MaterialTheme.colorScheme.onSurface),
                                        shape = CircleShape
                                    )
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun WeekObserver_Preview() {
    var selectedDate by remember {
        mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    }
    AppTheme {
        WeekObserver(selectedDate) {selectedDate = it}
    }
}

@Composable
fun MonthObserver(selectedDate: LocalDate) {
    LazyVerticalGrid(GridCells.Fixed(7)) {
        val dates = getMonth(selectedDate)
        items(dates.size) { dateIndex ->
            if (dates[dateIndex] != null) {
                IconButton(onClick = { }) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(46.dp)
                            .height(46.dp)
                    ) {
                        Text(
                            text = dates[dateIndex]!!.dayOfMonth.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        androidx.compose.animation.AnimatedVisibility(
                            visible = (dates[dateIndex] == selectedDate),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        brush = SolidColor(MaterialTheme.colorScheme.onSurface),
                                        shape = CircleShape
                                    )
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            } else {
                Box(modifier = Modifier
                    .height(46.dp)
                    .width(46.dp))
            }

        }
    }
}



fun getWeek(date: LocalDate): List<LocalDate> {
    val weekFields = WeekFields.of(Locale.getDefault())
    val firstDayOfWeek = weekFields.firstDayOfWeek.value
    val dateDayOfWek = date.dayOfWeek.value
    return (0..6).map { dayIndex ->
        date.plus(firstDayOfWeek % 7 - dateDayOfWek + dayIndex, unit = DateTimeUnit.DAY)
    }
}

fun getMonth(date: LocalDate): List<LocalDate?> {
    val weekFields = WeekFields.of(Locale.getDefault())
    val firstDayOfWeek = weekFields.firstDayOfWeek.value
    val start = LocalDate(date.year, date.month, 1)
    val end = start.plus(1, DateTimeUnit.MONTH)
    val daysInMonth = start.until(end, DateTimeUnit.DAY)
    val dayOfWeek = start.dayOfWeek.value
    val deltaDays = dayOfWeek - firstDayOfWeek % 7
    return (0 ..< deltaDays).map { null } + (0 ..< daysInMonth).map { start.plus(it, DateTimeUnit.DAY) }
}



@Preview(
    //showBackground = true,
    locale = "en"
)
@Composable
fun Calendar_Preview() {
    AppTheme {
        Calendar()
    }
}