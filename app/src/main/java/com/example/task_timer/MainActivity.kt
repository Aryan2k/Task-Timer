package com.example.task_timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.task_timer.ui.theme.TaskTimerTheme
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentApiVersion = android.os.Build.VERSION.SDK_INT

                    if (currentApiVersion >= android.os.Build.VERSION_CODES.O) {
                        val time = Clock.System.now().epochSeconds / 1000 + 7200 // Adding 2 hours
                        StopwatchTimer(seconds = time, currentApiVersion = currentApiVersion)
                    } else {
                        val time = System.currentTimeMillis()
                        StopwatchTimer(seconds = time, currentApiVersion = currentApiVersion)
                    }
                }
            }
        }
    }
}

@Composable
fun StopwatchTimer(seconds: Long, scale: Float = 1.0F, currentApiVersion: Int) {

    val elapsedTime: MutableState<Long> =
        if (currentApiVersion >= android.os.Build.VERSION_CODES.O) {
            remember {
                mutableStateOf(seconds - (Clock.System.now().epochSeconds / 1000))
            }
        } else {
            remember {
                mutableStateOf(seconds - (System.currentTimeMillis() / 1000))
            }
        }

    LaunchedEffect(true) {
        while (true) {
            delay(1000)
            elapsedTime.value -= 1
        }
    }

    Text(
        text = getRemainingTime(elapsedTime.value.toInt()),
        fontSize = if (scale == 1.0F) 24.sp else 12.sp,
        color = Color.Black,
    )
}

fun getRemainingTime(time: Int): String {
    return if (time > 0) {
        val hour = time / 3600
        val minute = time % 3600 / 60
        val sec = time % 60
        val str: String = if (hour == 0) {
            String.format(" %02d:%02d", minute, sec)
        } else { /*  w ww  .j  av  a2s  .c om*/
            String.format(" %02d:%02d:%02d", hour, minute, sec)
        }
        str
    } else {
        ""
    }
}