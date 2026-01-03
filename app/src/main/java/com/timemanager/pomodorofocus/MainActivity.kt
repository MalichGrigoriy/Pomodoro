package com.timemanager.pomodorofocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme
import com.timemanager.pomodorofocus.ui.timer.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PomodoroApp()
        }
    }
}

@Composable
fun PomodoroApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val timerViewMode: TimerViewModel = hiltViewModel()
    PomodoroFocusTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = { BottomNavigationBar(navController) }
        ) { innerPadding ->
            NavigationGraph(
                navController,
                timerViewMode,
                Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PomodoroFocusTheme {
        PomodoroApp()
    }
}
