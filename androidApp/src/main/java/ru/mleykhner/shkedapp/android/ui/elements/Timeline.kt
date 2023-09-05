package ru.mleykhner.shkedapp.android.ui.elements


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.android.ui.theme.weekdaysStyle
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.Month
import java.util.Collections

@Composable
fun Timeline(
    initialDate: LocalDate,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    visibleMonth: Month,
    onVisibleMonthChange: (Month) -> Unit
) {

    val density = LocalDensity.current
    val dateSize = 46
    val gapWidth = 12
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

    LaunchedEffect(widthDp) {
        daysOnScreen = (widthDp / (dateSize + gapWidth).dp).toInt() + 2
    }

    LaunchedEffect(dragOffset) {
        viewOffset = (with(density) { dragOffset.toDp() }.value % (dateSize + gapWidth)).dp
        dateOffset = with(density) { dragOffset.toDp() }.value.toInt() / (dateSize + gapWidth)
    }

    LaunchedEffect(dateOffset) {
        var hasVisibleMonth = false
        var newMonth = visibleMonth
        for (it in 0..daysOnScreen) {
            val id = it - dateOffset
            val date = initialDate.plusDays(id.toLong())
            if (date.month == visibleMonth) {
                hasVisibleMonth = true
                break
            }
            newMonth = date.month
        }
        if (!hasVisibleMonth) {
            onVisibleMonthChange(newMonth)
        }
    }

    Box (
        modifier = Modifier
            .scrollable(
                orientation = Orientation.Horizontal,
                state = rememberScrollableState { delta ->
                    dragOffset += delta
                    delta
                }
            )
            .onSizeChanged { size ->
                widthDp = with(density) {
                    size.width.toDp()
                }
            }
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(gapWidth.dp),
            modifier = Modifier
                .offset(x = viewOffset)
                .requiredWidth(IntrinsicSize.Min)

        ) {
            (0..daysOnScreen).forEach {
                val id = it - dateOffset
                val date = initialDate.plusDays(id.toLong())
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(dateSize.dp)
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
                        if (date.isEqual(selectedDate)) {
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
    val initialDate = LocalDate.now()
    var selectedDate by remember {
        mutableStateOf(initialDate)
    }
    var visibleMonth by remember {
        mutableStateOf(selectedDate.month)
    }
    AppTheme {
        Surface {
            Timeline(
                initialDate,
                selectedDate,
                { selectedDate = it },
                visibleMonth,
                { visibleMonth = it })
        }

    }
}

fun getShortWeekdaysSymbols(): List<String> {
    val formatter = DateFormatSymbols.getInstance()
    val names = formatter.shortWeekdays.drop(1).toList()
    Collections.rotate(names, -1)
    return names
}