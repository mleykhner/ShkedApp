package ru.mleykhner.shkedapp.android.ui.elements


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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import ru.mleykhner.shkedapp.android.R
import ru.mleykhner.shkedapp.android.ui.theme.weekdaysStyle
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Collections
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar() {
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            skipHiddenState = true,
            initialValue = SheetValue.PartiallyExpanded
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarControls(state: SheetValue) {
    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val context = LocalContext.current
    val controlsMotionScene = remember {
        context.resources
            .openRawResource(R.raw.calendar_controls_transition)
            .readBytes()
            .decodeToString()
    }
    val sheetMotionScene = remember {
        context.resources
            .openRawResource(R.raw.calendar_bottom_sheet_transition)
            .readBytes()
            .decodeToString()
    }
    val weekdaysNames = remember {
        getShortWeekdaysSymbols()
    }
    var monthLabel by remember { mutableStateOf(getMonthLabel(selectedDate)) }
    val progress by animateFloatAsState(if (state == SheetValue.Expanded) 1f else 0f, label = "")
    val weekPagerState = rememberPagerState(
        pageCount = { 3 },
        initialPage = 1
    )

    LaunchedEffect(weekPagerState) {
        snapshotFlow { weekPagerState.settledPage }.collect { page ->
            if (page != 1) {
                selectedDate = selectedDate.plusWeeks((page - 1).toLong())
                weekPagerState.scrollToPage(1)
            }
        }
    }

    LaunchedEffect(selectedDate) {
        monthLabel = getMonthLabel(selectedDate)
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
                Icon(Icons.Rounded.ArrowForward, contentDescription = null)
            }
            FilledTonalIconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .layoutId("previousButton")
            ) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = null)
            }
        }
        HorizontalPager(state = weekPagerState) { page ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                getWeek(selectedDate.plusWeeks((page - 1).toLong())).forEach { date ->
                    IconButton(onClick = {selectedDate = date}) {
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
                                visible = (date.isEqual(selectedDate)),
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
        MotionLayout(motionScene = MotionScene(content = sheetMotionScene), progress = progress) {
            Box(modifier = Modifier.layoutId("serviceBox"))
        }
    }
}

fun getMonthLabel(date: LocalDate): String {
    return date.month.getDisplayName(
        TextStyle.FULL_STANDALONE,
        Locale.getDefault()
    )
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
}

fun getWeek(date: LocalDate): List<LocalDate> {
    val weekFields = WeekFields.of(Locale.getDefault())
    val firstDayOfWeek = weekFields.firstDayOfWeek
    val weekStart = date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
    return (0..6).map { dayIndex ->
        weekStart.plusDays(dayIndex.toLong())
    }
}

fun getShortWeekdaysSymbols(): List<String> {
    val formatter = DateFormatSymbols.getInstance()
    val weekFields = WeekFields.of(Locale.getDefault())
    val firstDayOfWeek = weekFields.firstDayOfWeek.value
    val names = formatter.shortWeekdays.drop(1)
    Collections.rotate(names, -firstDayOfWeek)
    return names
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