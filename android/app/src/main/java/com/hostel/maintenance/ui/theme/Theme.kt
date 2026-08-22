package com.hostel.maintenance.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Blue = Color(0xFF2563EB)
private val Background = Color(0xFFF4F6F8)

private val ColorScheme = lightColorScheme(
    primary = Blue,
    background = Background,
    surface = Color.White,
)

@Composable
fun HostelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        content = content,
    )
}
