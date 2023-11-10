package ru.mleykhner.shkedapp.android.ui.elements

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Preview(widthDp = 360, showBackground = true)
@Composable
fun SchedulePager() {

    val density = LocalDensity.current
    var widthDp by remember {
        mutableStateOf(0.dp)
    }

    val anchors = with (density) {
        DraggableAnchors {
            -1f at -(widthDp.toPx())
            0f at 0f
            1f at widthDp.toPx()
        }
    }

    val state = remember { AnchoredDraggableState(
        initialValue = 0f,
        positionalThreshold = { totalDistance: Float ->  totalDistance * 0.5f },
        velocityThreshold = { with(density) { 125.dp.toPx() } },
        animationSpec = tween()
    ) }

    var index by remember {
        mutableIntStateOf(0)
    }

    SideEffect {
        state.updateAnchors(anchors)
    }

    LaunchedEffect(state.currentValue) {
        if (state.currentValue > 0) index -= 1
        else if (state.currentValue < 0) index += 1
        state.snapTo(0f)
    }

    Box(
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
                            .toInt(), y = 0
                    )
                }
                .requiredWidth(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.width(widthDp)
            ) {
                Text(text = "${index - 1}")
            }
            Column(
                modifier = Modifier.width(widthDp)
            ) {
                Text(text = "$index")
            }
            Column(
                modifier = Modifier.width(widthDp)
            ) {
                Text(text = "${index + 1}")
            }
        }
    }
}