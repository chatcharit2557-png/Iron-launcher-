package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IronCyan
import com.example.ui.theme.IronSurface
import com.example.ui.theme.IronSurfaceBorder
import com.example.ui.viewmodel.NavigationDestination

@Composable
fun BottomNavBar(
  currentDestination: NavigationDestination,
  onNavigate: (NavigationDestination) -> Unit
) {
  NavigationBar(
    containerColor = IronSurface,
    modifier = Modifier.border(width = 1.dp, color = IronSurfaceBorder)
  ) {
    NavigationBarItem(
      selected = currentDestination is NavigationDestination.Home,
      onClick = { onNavigate(NavigationDestination.Home) },
      icon = {
        Icon(
          imageVector = Icons.Default.PlayCircle,
          contentDescription = "Launcher Home"
        )
      },
      label = { Text("Home", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IronCyan,
        selectedTextColor = IronCyan,
        unselectedIconColor = Color(0xFF8B949E),
        unselectedTextColor = Color(0xFF8B949E),
        indicatorColor = Color(0xFF00363D)
      ),
      modifier = Modifier.testTag("nav_home_button")
    )

    NavigationBarItem(
      selected = currentDestination is NavigationDestination.Profiles,
      onClick = { onNavigate(NavigationDestination.Profiles) },
      icon = {
        Icon(
          imageVector = Icons.Default.Layers,
          contentDescription = "Profiles & Versions"
        )
      },
      label = { Text("Profiles", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IronCyan,
        selectedTextColor = IronCyan,
        unselectedIconColor = Color(0xFF8B949E),
        unselectedTextColor = Color(0xFF8B949E),
        indicatorColor = Color(0xFF00363D)
      ),
      modifier = Modifier.testTag("nav_profiles_button")
    )

    NavigationBarItem(
      selected = currentDestination is NavigationDestination.Mods,
      onClick = { onNavigate(NavigationDestination.Mods) },
      icon = {
        Icon(
          imageVector = Icons.Default.Extension,
          contentDescription = "Mods & Fabric API"
        )
      },
      label = { Text("Mods & API", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IronCyan,
        selectedTextColor = IronCyan,
        unselectedIconColor = Color(0xFF8B949E),
        unselectedTextColor = Color(0xFF8B949E),
        indicatorColor = Color(0xFF00363D)
      ),
      modifier = Modifier.testTag("nav_mods_button")
    )

    NavigationBarItem(
      selected = currentDestination is NavigationDestination.Server,
      onClick = { onNavigate(NavigationDestination.Server) },
      icon = {
        Icon(
          imageVector = Icons.Default.Dns,
          contentDescription = "Local Server Host"
        )
      },
      label = { Text("Host Server", fontSize = 11.sp) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = IronCyan,
        selectedTextColor = IronCyan,
        unselectedIconColor = Color(0xFF8B949E),
        unselectedTextColor = Color(0xFF8B949E),
        indicatorColor = Color(0xFF00363D)
      ),
      modifier = Modifier.testTag("nav_server_button")
    )
  }
}
