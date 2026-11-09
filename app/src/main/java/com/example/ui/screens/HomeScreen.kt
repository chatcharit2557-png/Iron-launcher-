package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GameProfile
import com.example.data.model.ModLoader
import com.example.data.model.ServerStatus
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.FabricPurple
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.ForgeOrange
import com.example.ui.theme.IronCyan
import com.example.ui.theme.IronDarkBg
import com.example.ui.theme.IronSurface
import com.example.ui.theme.IronSurfaceBorder
import com.example.ui.theme.IronSurfaceVariant
import com.example.ui.theme.RedstoneOffline
import com.example.ui.viewmodel.LauncherViewModel
import com.example.ui.viewmodel.NavigationDestination

@Composable
fun HomeScreen(
  viewModel: LauncherViewModel,
  onNavigateToProfiles: () -> Unit,
  onNavigateToMods: () -> Unit,
  onNavigateToServer: () -> Unit
) {
  val activeProfile by viewModel.activeProfile.collectAsState()
  val isLaunching by viewModel.isLaunching.collectAsState()
  val launchProgress by viewModel.launchProgress.collectAsState()
  val launchStatusText by viewModel.launchStatusText.collectAsState()
  val deviceHardware by viewModel.deviceHardware.collectAsState()
  val localIp by viewModel.localIp.collectAsState()
  val serverStatus by viewModel.serverStatus.collectAsState()
  val serverConfig by viewModel.serverConfig.collectAsState()
  val mods by viewModel.mods.collectAsState()

  val animatedProgress by animateFloatAsState(
    targetValue = launchProgress,
    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
    label = "launch_progress"
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(IronDarkBg)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      // Hero Banner with Iron Fortress & Steve
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .clip(RoundedCornerShape(16.dp))
          .border(1.5.dp, IronSurfaceBorder, RoundedCornerShape(16.dp))
      ) {
        Image(
          painter = painterResource(id = R.drawable.iron_banner),
          contentDescription = "Iron Launcher Hero Banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        // Cyber Gradient Overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  Color(0xCC0D1117),
                  Color(0xF50D1117)
                ),
                startY = 60f
              )
            )
        )
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
          verticalArrangement = Arrangement.Bottom
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0x8000363D))
              .border(1.dp, IronCyan, RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Speed,
              contentDescription = null,
              tint = IronCyan,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "OPTIMIZED JAVA RUNTIME",
              color = IronCyan,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "IRON LAUNCHER",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
          )
          Text(
            text = "Minecraft Java Client • Mod Manager • Local Server Host",
            color = Color(0xFFBAC7D5),
            fontSize = 12.sp
          )
        }
      }
    }

    // Main Game Launch Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.5.dp, IronCyan, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = IronSurface),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Selected Profile Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "SELECTED PROFILE",
                color = Color(0xFF8B949E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              )
              Text(
                text = activeProfile?.name ?: "No Profile Selected",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
            // Switch Profile Button
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(IronSurfaceVariant)
                .border(1.dp, IronSurfaceBorder, RoundedCornerShape(8.dp))
                .clickable(onClick = onNavigateToProfiles)
                .testTag("switch_profile_button")
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Layers,
                  contentDescription = null,
                  tint = IronCyan,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Switch",
                  color = IronCyan,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Profile Badges (Version, ModLoader, Fabric API status, RAM)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Version badge (e.g. 1.21.4, 26.3, 26.2)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF21262D))
                .border(1.dp, IronSurfaceBorder, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "MC ${activeProfile?.gameVersion ?: "1.21.4"}",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            // Loader Badge (Fabric / Forge / Vanilla)
            val loader = activeProfile?.modLoader ?: ModLoader.FABRIC
            val loaderColor = Color(loader.badgeColorHex)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(loaderColor.copy(alpha = 0.2f))
                .border(1.dp, loaderColor, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = loader.displayName,
                color = loaderColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }

            // Fabric API indicator if Fabric
            if (loader == ModLoader.FABRIC) {
              val hasFabricApi = activeProfile?.fabricApiInstalled == true
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (hasFabricApi) Color(0xFF00363D) else Color(0xFF3B1E22))
                  .border(1.dp, if (hasFabricApi) IronCyan else RedstoneOffline, RoundedCornerShape(6.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = if (hasFabricApi) "Fabric API ✓" else "Fabric API Required",
                  color = if (hasFabricApi) IronCyan else RedstoneOffline,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // RAM Allocation
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF21262D))
                .border(1.dp, IronSurfaceBorder, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "${activeProfile?.allocatedRamMb ?: 2048} MB RAM",
                color = Color(0xFF8B949E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Launch Progress Bar if active
          AnimatedVisibility(visible = isLaunching) {
            Column(modifier = Modifier.padding(bottom = 12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = launchStatusText,
                  color = IronCyan,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
                Text(
                  text = "${(animatedProgress * 100).toInt()}%",
                  color = IronCyan,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(6.dp)
                  .clip(RoundedCornerShape(3.dp)),
                color = IronCyan,
                trackColor = IronSurfaceVariant
              )
            }
          }

          // Launch Button
          Button(
            onClick = { viewModel.launchGame() },
            enabled = !isLaunching,
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("launch_game_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = IronCyan,
              contentColor = Color(0xFF00363D)
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              if (isLaunching) {
                CircularProgressIndicator(
                  modifier = Modifier.size(20.dp),
                  color = Color(0xFF00363D),
                  strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = "LAUNCHING MINECRAFT...",
                  fontWeight = FontWeight.Black,
                  fontSize = 15.sp,
                  letterSpacing = 0.5.sp
                )
              } else {
                Icon(
                  imageVector = Icons.Default.PlayArrow,
                  contentDescription = null,
                  modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "PLAY / เข้าเล่นเกม",
                  fontWeight = FontWeight.Black,
                  fontSize = 16.sp,
                  letterSpacing = 0.5.sp
                )
              }
            }
          }
        }
      }
    }

    // Local Host Server Quick Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, IronSurfaceBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = IronSurface),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF21262D)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Dns,
                  contentDescription = null,
                  tint = ForgeAmber,
                  modifier = Modifier.size(18.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "LOCAL MINECRAFT SERVER",
                  color = Color(0xFF8B949E),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${serverConfig.serverType.displayName} (${serverConfig.gameVersion})",
                  color = Color.White,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // Server Running indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
              val (color, text) = when (serverStatus) {
                ServerStatus.RUNNING -> EmeraldOnline to "ONLINE"
                ServerStatus.STARTING -> ForgeAmber to "STARTING"
                ServerStatus.STOPPING -> ForgeAmber to "STOPPING"
                ServerStatus.OFFLINE -> RedstoneOffline to "OFFLINE"
              }
              Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(8.dp)
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = text,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // IP & Specs info
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(IronSurfaceVariant)
              .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = null,
                tint = IronCyan,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "LAN IP: $localIp:${serverConfig.port}",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
              )
            }
            Text(
              text = "Max RAM: ${serverConfig.maxRamMb}MB",
              color = Color(0xFF8B949E),
              fontSize = 11.sp
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                if (serverStatus == ServerStatus.RUNNING) {
                  viewModel.stopServer()
                } else {
                  viewModel.startServer()
                }
              },
              modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .testTag("home_server_toggle_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (serverStatus == ServerStatus.RUNNING) RedstoneOffline else EmeraldOnline,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = if (serverStatus == ServerStatus.RUNNING) "Stop Server" else "Start Server",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }

            Button(
              onClick = onNavigateToServer,
              modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .testTag("home_server_terminal_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = IronSurfaceVariant,
                contentColor = IronCyan
              ),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Terminal,
                contentDescription = null,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Console & Config",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          }
        }
      }
    }

    // Device Hardware Telemetry Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, IronSurfaceBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = IronSurface),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Memory,
                contentDescription = null,
                tint = IronCyan,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "DEVICE HARDWARE SPECS",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
            IconButton(
              onClick = { viewModel.refreshHardwareInfo() },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = Color(0xFF8B949E),
                modifier = Modifier.size(16.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // RAM Metric
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(IronSurfaceVariant)
                .padding(10.dp)
            ) {
              Column {
                Text(
                  text = "TOTAL RAM",
                  color = Color(0xFF8B949E),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${deviceHardware.totalRamMb} MB",
                  color = Color.White,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Free: ${deviceHardware.freeRamMb} MB",
                  color = EmeraldOnline,
                  fontSize = 11.sp
                )
              }
            }

            // CPU Cores Metric
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(IronSurfaceVariant)
                .padding(10.dp)
            ) {
              Column {
                Text(
                  text = "CPU CORES",
                  color = Color(0xFF8B949E),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${deviceHardware.cpuCores} Physical Cores",
                  color = Color.White,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Multi-thread GC Ready",
                  color = IronCyan,
                  fontSize = 11.sp
                )
              }
            }
          }
        }
      }
    }

    // Fabric API & Mods Quick Shortcut Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, IronSurfaceBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = IronSurface),
        shape = RoundedCornerShape(14.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigateToMods)
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(FabricPurple.copy(alpha = 0.2f))
                .border(1.dp, FabricPurple, RoundedCornerShape(8.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Extension,
                contentDescription = null,
                tint = FabricPurple,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Mod Manager & Fabric API",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              val enabledCount = mods.count { it.isEnabled }
              Text(
                text = "$enabledCount active mods • Fabric API ready",
                color = Color(0xFF8B949E),
                fontSize = 12.sp
              )
            }
          }
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = IronCyan,
            modifier = Modifier.size(18.dp)
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
