package com.timemanager.pomodorofocus.ui.timer

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timemanager.pomodorofocus.R
import com.timemanager.pomodorofocus.ui.components.SharedTimerSizeState
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme
import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType

@Composable
fun TimerScreen(
    sharedState: SharedTimerSizeState,
    timerViewModel: TimerViewModel,
) {
    val minutes by timerViewModel.minutes.collectAsStateWithLifecycle()
    val seconds by timerViewModel.seconds.collectAsStateWithLifecycle()
    val milliseconds by timerViewModel.milliseconds.collectAsStateWithLifecycle()
    val millisecondsFull by timerViewModel.millisecondsFull.collectAsStateWithLifecycle()
    val isNegativeColor by timerViewModel.isNegativeColor.collectAsStateWithLifecycle()
    val isNegativeMark by timerViewModel.isNegativeMark.collectAsStateWithLifecycle()
    val action by timerViewModel.action.collectAsStateWithLifecycle()
    val selectedTimerType by timerViewModel.selectedTimerType.collectAsStateWithLifecycle()
    val task by timerViewModel.task.collectAsStateWithLifecycle()

    val timerState = TimerStateHolder(
        minutes = minutes,
        seconds = seconds,
        milliseconds = milliseconds,
        millisecondsFull = millisecondsFull,
        isNegativeColor = isNegativeColor,
        isNegativeMark = isNegativeMark,
        action = action,
        selectedTimerType = selectedTimerType,
    )

    TimerScreenConstraint(
        timerState = timerState,
        task = task,
        sharedState = sharedState,
        onTaskChange = { text: String -> timerViewModel.changeTask(text) },
        onTaskSave = { timerViewModel.saveTask() },
        onClick = { timerViewModel.onTimerClick() },
    )

    ClearFocusByKeyboard()
}

@Composable
fun TimerScreenConstraint(
    timerState: TimerStateHolder,
    task: String,
    sharedState: SharedTimerSizeState,
    onTaskChange: (String) -> Unit,
    onTaskSave: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val focusManager = LocalFocusManager.current
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() }) // Clears focus on tap outside
            },
    ) {
        val (taskInput, timer) = createRefs()

        TextField(
            value = task,
            onValueChange = onTaskChange,
            placeholder = { Text(text = stringResource(id = R.string.timer_task_hint)) },
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f),
            ),
            modifier = modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 32.dp)
                .padding(bottom = 32.dp)
                .semantics { contentDescription = "Task input" }
                .onFocusChanged { focus -> if (!focus.isFocused) onTaskSave.invoke() }
                .constrainAs(taskInput) {
                    bottom.linkTo(timer.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        TimerWithActions(
            timerState,
            Modifier
                .scale(sharedState.percentSize)
                .aspectRatio(1f)
                .constrainAs(timer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .clip(CircleShape)
                .clickable(
                    onClick = {
                        focusManager.clearFocus()
                        onClick()
                    }
                )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ClearFocusByKeyboard() {
    val focusManager = LocalFocusManager.current
    val imeVisible = WindowInsets.isImeVisible
    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus()
        }
    }
}

@ExperimentalLayoutApi
@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF000000
)
@Composable
private fun PreviewTimerScreen() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        TimerScreenConstraint(
            timerState = TimerStateHolder(
                minutes = 12,
                seconds = 34,
                milliseconds = 39,
                millisecondsFull = 123123252,
                action = TimerAction.RESTART,
                isNegativeMark = false,
                isNegativeColor = true,
                selectedTimerType = TimerType.LedMatrix,
            ),
            task = "Some long Task",
            sharedState = SharedTimerSizeState(0.8f, Offset(0f, 0f)),
            onTaskChange = {},
            onTaskSave = {},
            onClick = {},
        )
    }
}
