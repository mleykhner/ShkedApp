package ru.mleykhner.shkedapp.android.ui.elements

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.utils.getMonthLabel
import ru.mleykhner.shkedapp.vm.ScheduleScreenViewModel

@Composable
fun TimelineControls(
    modifier: Modifier = Modifier,
    viewModel: ScheduleScreenViewModel
) {
    var selectedDate by remember {
        mutableStateOf(viewModel.selectedDate)
    }

    var visibleMonth by remember {
        mutableStateOf(selectedDate.month)
    }

    var visibleMonthLabel by remember {
        mutableStateOf(getMonthLabel(visibleMonth))
    }

    var isMenuExpanded by remember {
        mutableStateOf(false)
    }

    viewModel.actions.observeAsActions { action ->
        when (action) {
            ScheduleScreenViewModel.Action.DateChanged -> {
                selectedDate = viewModel.selectedDate
                visibleMonth = selectedDate.month
            }
            ScheduleScreenViewModel.Action.Failed -> Log.e("TimelineControls", "Failed")
            ScheduleScreenViewModel.Action.HasChanges -> Log.v("TimelineControls", "Has Changes")
            ScheduleScreenViewModel.Action.NoConnection -> Log.e("TimelineControls", "No Connection")
            ScheduleScreenViewModel.Action.Refreshed -> Log.i("TimelineControls", "Refreshed")
        }
    }

    LaunchedEffect(visibleMonth) {
        visibleMonthLabel = getMonthLabel(visibleMonth)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
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
                Box {
                    IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                        Icon(Icons.Rounded.MoreVert, "more")
                    }
                    DropdownMenu(
                        isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                   Text(text = "Back to today")
                            },
                            onClick = {
                                viewModel.backToToday()
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Timeline(
                modifier = Modifier.padding(bottom = 12.dp),
                viewModel = viewModel,
                visibleMonth
            ) { visibleMonth = it }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 370
)
@Composable
fun TimelineControls_Preview() {
    val vm = remember {
        ScheduleScreenViewModel()
    }
    AppTheme {
        TimelineControls(viewModel = vm)
    }
}