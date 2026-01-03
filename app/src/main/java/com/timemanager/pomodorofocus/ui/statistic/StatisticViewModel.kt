package com.timemanager.pomodorofocus.ui.statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timemanager.pomodorofocus.domain.model.StatisticListItem
import com.timemanager.pomodorofocus.domain.statistic.TimerStatisticUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val timerStatisticUseCase: TimerStatisticUseCase
) : ViewModel() {

    val taskGroups: Flow<PagingData<StatisticListItem>> = timerStatisticUseCase()
        .cachedIn(viewModelScope)
}
