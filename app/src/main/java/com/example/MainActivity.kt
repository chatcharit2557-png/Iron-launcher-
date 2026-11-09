package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppTopBar
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.AccountDialog
import com.example.ui.screens.GameSessionScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ModsScreen
import com.example.ui.screens.ProfilesScreen
import com.example.ui.screens.ServerScreen
import com.example.ui.theme.IronDarkBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.LauncherViewModel
import com.example.ui.viewmodel.NavigationDestination

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        IronLauncherApp()
      }
    }
  }
}

@Composable
fun IronLauncherApp(viewModel: LauncherViewModel = viewModel()) {
  val isGameSessionActive by viewModel.isGameSessionActive.collectAsState()
  val currentDestination by viewModel.currentDestination.collectAsState()
  val playerAccount by viewModel.playerAccount.collectAsState()
  val serverStatus by viewModel.serverStatus.collectAsState()
  val deviceHardware by viewModel.deviceHardware.collectAsState()

  var showAccountDialog by remember { mutableStateOf(false) }

  // If in game session, show full-screen game canvas and controls
  if (isGameSessionActive) {
    GameSessionScreen(viewModel = viewModel)
  } else {
    // Standard back handler to return to Home when on sub-screens
    if (currentDestination !is NavigationDestination.Home) {
      BackHandler {
        viewModel.navigateTo(NavigationDestination.Home)
      }
    }

    Scaffold(
      modifier = Modifier
        .fillMaxSize()
        .background(IronDarkBg),
      containerColor = IronDarkBg,
      topBar = {
        AppTopBar(
          playerAccount = playerAccount,
          serverStatus = serverStatus,
          deviceHardware = deviceHardware,
          onAccountClick = { showAccountDialog = true },
          onServerClick = { viewModel.navigateTo(NavigationDestination.Server) }
        )
      },
      bottomBar = {
        BottomNavBar(
          currentDestination = currentDestination,
          onNavigate = { viewModel.navigateTo(it) }
        )
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        Crossfade(targetState = currentDestination, label = "screen_transition") { dest ->
          when (dest) {
            is NavigationDestination.Home -> HomeScreen(
              viewModel = viewModel,
              onNavigateToProfiles = { viewModel.navigateTo(NavigationDestination.Profiles) },
              onNavigateToMods = { viewModel.navigateTo(NavigationDestination.Mods) },
              onNavigateToServer = { viewModel.navigateTo(NavigationDestination.Server) }
            )
            is NavigationDestination.Profiles -> ProfilesScreen(viewModel = viewModel)
            is NavigationDestination.Mods -> ModsScreen(viewModel = viewModel)
            is NavigationDestination.Server -> ServerScreen(viewModel = viewModel)
            is NavigationDestination.Account -> HomeScreen(
              viewModel = viewModel,
              onNavigateToProfiles = { viewModel.navigateTo(NavigationDestination.Profiles) },
              onNavigateToMods = { viewModel.navigateTo(NavigationDestination.Mods) },
              onNavigateToServer = { viewModel.navigateTo(NavigationDestination.Server) }
            )
          }
        }
      }
    }

    if (showAccountDialog) {
      AccountDialog(
        viewModel = viewModel,
        onDismiss = { showAccountDialog = false }
      )
    }
  }
}
