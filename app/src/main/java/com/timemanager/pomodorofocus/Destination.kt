package com.timemanager.pomodorofocus

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.timemanager.pomodorofocus.ui.icons.BaselineAccessTime24

interface Destination {
    val icon: ImageVector
    val route: String
}

object TimerDestination : Destination {
    override val icon = BaselineAccessTime24
    override val route = "timer"
}

object SettingsDestination : Destination {
    override val icon = Icons.Default.Settings
    override val route = "settings"
}

object TaskManagerDestination : Destination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val route = "tasks"
}

val bottomNavigationList = listOf(TaskManagerDestination, TimerDestination, SettingsDestination)
