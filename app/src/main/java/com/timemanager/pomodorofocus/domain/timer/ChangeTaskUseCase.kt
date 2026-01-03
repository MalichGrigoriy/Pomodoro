package com.timemanager.pomodorofocus.domain.timer

import com.timemanager.pomodorofocus.domain.model.Task
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class ChangeTaskUseCase @Inject constructor(private val repository: SchedulerRepository) {
    suspend operator fun invoke(id: UUID, text: String) = withContext(Dispatchers.IO) {
        repository.timerOrNull(id)?.let {
            repository.changeTimer(it.change(task = Task(text)))
        }
    }
}
