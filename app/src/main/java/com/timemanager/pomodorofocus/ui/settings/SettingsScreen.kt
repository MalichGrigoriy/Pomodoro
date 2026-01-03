package com.timemanager.pomodorofocus.ui.settings

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timemanager.pomodorofocus.domain.settings.TIMER_TYPE_LIST
import com.timemanager.pomodorofocus.ui.components.SharedTimerSizeState
import com.timemanager.pomodorofocus.ui.timer.TimerAction
import com.timemanager.pomodorofocus.ui.timer.TimerStateHolder
import com.timemanager.pomodorofocus.ui.timer.TimerViewModel
import com.timemanager.pomodorofocus.ui.timer.TimerWithActions
import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.cbrt
import kotlin.math.pow

@Immutable
data class StableTimerTypeList(val items: List<TimerType>)

@Composable
fun SettingsScreen(
    sharedState: SharedTimerSizeState,
    viewModel: SettingsViewModel = hiltViewModel(),
    timerViewModel: TimerViewModel,
    navigateToMain: () -> Unit
) {
    val size = Int.MAX_VALUE
    val initData by viewModel.typeListWithSelected.collectAsStateWithLifecycle()

    val minutes by timerViewModel.minutes.collectAsStateWithLifecycle()
    val seconds by timerViewModel.seconds.collectAsStateWithLifecycle()
    val milliseconds by timerViewModel.milliseconds.collectAsStateWithLifecycle()
    val millisecondsFull by timerViewModel.millisecondsFull.collectAsStateWithLifecycle()
    val isNegativeColor by timerViewModel.isNegativeColor.collectAsStateWithLifecycle()
    val isNegativeMark by timerViewModel.isNegativeMark.collectAsStateWithLifecycle()
    val action by timerViewModel.action.collectAsStateWithLifecycle()
    val selectedTimerType by timerViewModel.selectedTimerType.collectAsStateWithLifecycle()

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

    val initialIndex = remember(initData) {
        findValidIndex(size, initData.first, initData.second)
    }

    val pagerState = key(initialIndex) {
        rememberPagerState(
            initialPage = initialIndex,
            pageCount = { size }
        )
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        val newTimer = initData.first[pagerState.currentPage % initData.first.size]
        val isProgrammaticChanges = pagerState.isScrollInProgress
        viewModel.setTimerType(newTimer, isProgrammaticChanges)
    }

    SettingsScreenHolder(
        pagerState = pagerState,
        initTimer = initData.second,
        timerTypeList = StableTimerTypeList(initData.first),
        sharedState = sharedState,
        timerState = timerState,
        navigateToMain = navigateToMain,
    )
}

fun findValidIndex(listSize: Int, timerTypes: List<TimerType>, initType: TimerType): Int {
    val index = timerTypes.indexOf(initType)
    val halfIndex = listSize / 2
    val returnValue = (halfIndex until listSize).firstOrNull { it % timerTypes.size == index }
        ?: (listSize / 2)
    return returnValue
}

@Composable
fun SettingsScreenHolder(
    pagerState: PagerState,
    initTimer: TimerType,
    timerTypeList: StableTimerTypeList,
    sharedState: SharedTimerSizeState,
    timerState: TimerStateHolder,
    navigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        BoxWithConstraints(
            modifier = Modifier
        ) {
            val size = this.maxWidth * sharedState.percentSize
            val padding = (this.maxWidth - size) / 2

            CompositionLocalProvider(LocalOverscrollFactory provides null) {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = -size * 0.45f,
                    contentPadding = PaddingValues(start = padding),
                    pageSize = PageSize.Fixed(size),
                ) { page ->
                    val timerType = timerTypeList.items.getOrNull(page % timerTypeList.items.size)
                    key(timerType, initTimer) {
                        timerType?.let { timerType ->
                            val scale = calculateScale(pagerState, page)
                            val progress = calculateProgress(pagerState, page)
                            TimerCard(
                                timerType = timerType,
                                timerState = timerState,
                                onClick = onClick(
                                    coroutineScope,
                                    pagerState,
                                    page,
                                    navigateToMain,
                                ),
                                modifier = Modifier
                                    .padding(vertical = size / 2.9f)
                                    .offset(y = size / 1.76f * progress)
                                    .scale(scale)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun onClick(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    page: Int,
    navigateToMain: () -> Unit,
): (TimerType) -> Unit = {
    if (pagerState.isScrollInProgress.not()) {
        coroutineScope.launch {
            if (pagerState.currentPage == page) {
                navigateToMain()
            } else {
                pagerState.animateScrollToPage(page)
            }
        }
    }
}

@Composable
fun TimerCard(
    timerType: TimerType,
    timerState: TimerStateHolder,
    onClick: (TimerType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        TimerWithActions(
            TimerStateHolder(
                minutes = timerState.minutes,
                seconds = timerState.seconds,
                milliseconds = timerState.milliseconds,
                millisecondsFull = timerState.millisecondsFull,
                action = timerState.action,
                isNegativeMark = timerState.isNegativeMark,
                isNegativeColor = timerState.isNegativeColor,
                selectedTimerType = timerType,
            ),
            Modifier
                .aspectRatio(1f)
                .clickable { onClick(timerType) },
        )
    }
}

@Composable
fun calculateProgress(pagerState: PagerState, page: Int): Float {
    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
    return ((pageOffset.absoluteValue.pow(3)).coerceIn(0f, 1f))
}

@Composable
fun calculateScale(pagerState: PagerState, page: Int): Float {
    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
    val scale = 1f - (0.58f * cbrt(pageOffset.absoluteValue).coerceIn(0f, 1f))
    return scale
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {

    val initTimer = TimerType.String
    val timerList = StableTimerTypeList(TIMER_TYPE_LIST)
    val size = Int.MAX_VALUE

    val initialPage = findValidIndex(size, timerList.items, initTimer)

    val timerState = TimerStateHolder(
        minutes = 25,
        seconds = 0,
        milliseconds = 0,
        millisecondsFull = 1500000,
        isNegativeColor = false,
        isNegativeMark = false,
        action = TimerAction.START,
        selectedTimerType = TimerType.LedMatrix,
    )

    SettingsScreenHolder(
        pagerState = rememberPagerState(initialPage = initialPage, pageCount = { size }),
        initTimer = initTimer,
        timerTypeList = timerList,
        sharedState = SharedTimerSizeState(0.6f, Offset(0f, 0f)),
        timerState = timerState,
        navigateToMain = {}
    )
}
