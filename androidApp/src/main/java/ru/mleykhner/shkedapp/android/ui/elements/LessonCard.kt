package ru.mleykhner.shkedapp.android.ui.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mleykhner.shared_resources.SharedRes
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.data.LessonType
import ru.mleykhner.shkedapp.data.models.LessonViewData
import ru.mleykhner.shkedapp.utils.Strings
import ru.mleykhner.shkedapp.utils.toLocalizedString

@Composable
fun LessonCard(lesson: LessonViewData, modifier: Modifier = Modifier) {

    var expanded by remember {
        mutableStateOf(true)
    }
    val elevation by animateDpAsState(if (expanded) 3.dp else 1.dp, label = "cardElevation")

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = elevation,
        shadowElevation = elevation,
        modifier = modifier
            .clickable { expanded = !expanded }
    ) {
        Column {
            Row (
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Порядковый номер пары
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onSurface,
                                CircleShape
                            )
                            .height(42.dp)
                            .width(42.dp)
                    ) {
                        Text(
                            text = lesson.ordinal.toString(),
                            style = MaterialTheme
                                .typography
                                .titleMedium
                                .copy(fontFeatureSettings = "tnum"),
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(2.5.dp)
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme.onSurface,
                                RoundedCornerShape(5.dp)
                            )
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = Strings(LocalContext.current)
                            .get(SharedRes.strings.now, emptyList())
                            .uppercase(),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = lesson.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(8.dp))
                    Row (
                        modifier = Modifier
                            .height(26.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .border(
                                        2.4.dp,
                                        MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(5.dp)
                                    )
                            ) {
                                Text(
                                    text = lesson.type.toLocalizedString(LocalContext.current),
                                    style = MaterialTheme
                                        .typography
                                        .bodyMedium
                                        .copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .padding(
                                            vertical = 5.dp,
                                            horizontal = 8.dp
                                        )
                                )
                            }
                            Text(
                                text = lesson.location,
                                style = MaterialTheme
                                    .typography
                                    .bodyMedium
                                    .copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.secondary,
                            )
                            Text(
                                text = "9:00 – 10:30",
                                style = MaterialTheme
                                    .typography
                                    .bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .offset(x = 8.dp)
                                    .size(8.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        CircleShape
                                    )
                            )
                            IconButton(onClick = { expanded = !expanded }) {
                                AnimatedContent(
                                    expanded,
                                    label = "iconTransition"
                                ) {
                                    Icon(if (it) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown, null)
                                }
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                expanded,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .padding(bottom = 2.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.background,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp)

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = Strings(LocalContext.current)
                                    .get(SharedRes.strings.lecturer, emptyList()),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = lesson.lecturer ?: "–",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(
                            onClick = { /*TODO*/ },
                            enabled = !lesson.lecturer.isNullOrEmpty()
                        ) {
                            Icon(Icons.Rounded.Info, null)
                        }
                    }
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.Add, null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(Strings(LocalContext.current)
                            .get(SharedRes.strings.add_task, emptyList()))
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    locale = "ru"
)
@Composable
fun LessonCard_Preview() {
    val lesson = LessonViewData(
        name = "Основы схемотехники",
        lecturer = null,
        ordinal = 2,
        type = LessonType.PRACTICAL,
        location = "3-425"
    )

    AppTheme {
        LessonCard(lesson, Modifier.padding(12.dp))
    }
}