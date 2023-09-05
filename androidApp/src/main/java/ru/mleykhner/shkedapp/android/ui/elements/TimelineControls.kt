package ru.mleykhner.shkedapp.android.ui.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TimelineControls() {
    var initialDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var visibleMonth by remember {
        mutableStateOf(selectedDate.month)
    }

    var visibleMonthLabel by remember {
        mutableStateOf(getMonthLabel(visibleMonth))
    }

    LaunchedEffect(visibleMonth) {
        visibleMonthLabel = getMonthLabel(visibleMonth)
    }

    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 18.dp,
                        top = 4.dp
                    )
            ) {
                AnimatedContent (
                    targetState = visibleMonthLabel,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "MonthTransition"
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Rounded.MoreVert, "more")
                }
            }
            Timeline(
                modifier = Modifier
                    .padding(bottom = 12.dp),
                initialDate,
                selectedDate,
                { selectedDate = it },
                visibleMonth,
                { visibleMonth = it}
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 414
)
@Composable
fun TimelineControls_Preview() {
    AppTheme {
        TimelineControls()
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

fun getMonthLabel(month: Month): String {
    return month.getDisplayName(
        TextStyle.FULL_STANDALONE,
        Locale.getDefault()
    )
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
}