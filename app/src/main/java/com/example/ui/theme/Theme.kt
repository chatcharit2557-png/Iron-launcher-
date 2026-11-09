package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val IronColorScheme = darkColorScheme(
  primary = IronCyan,
  onPrimary = Color(0xFF00363D),
  primaryContainer = Color(0xFF004F58),
  onPrimaryContainer = IronCyanGlow,
  secondary = ForgeAmber,
  onSecondary = Color(0xFF452B00),
  secondaryContainer = Color(0xFF633F00),
  onSecondaryContainer = Color(0xFFFFDDB3),
  tertiary = FabricPurple,
  onTertiary = Color(0xFF381E72),
  background = IronDarkBg,
  onBackground = TextPrimary,
  surface = IronSurface,
  onSurface = TextPrimary,
  surfaceVariant = IronSurfaceVariant,
  onSurfaceVariant = TextSecondary,
  outline = IronSurfaceBorder,
  error = RedstoneOffline,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = IronColorScheme,
    typography = Typography,
    content = content
  )
}
