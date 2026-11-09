package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServerConfig
import com.example.data.model.ServerStatus
import com.example.data.model.ServerType
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.FabricPurple
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.ForgeOrange
import com.example.ui.theme.IronCyan
import com.example.ui.theme.IronDarkBg
import com.example.ui.theme.IronSurface
import com.example.ui.theme.IronSurfaceBorder
import com.example.ui.theme.IronSurfaceVariant
import com.example.ui.theme.PurpurMagenta
import com.example.ui.theme.RedstoneOffline
import com.example.ui.viewmodel.LauncherViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServerScreen(viewModel: LauncherViewModel) {
  val context = LocalContext.current
  val serverStatus by viewModel.serverStatus.collectAsState()
  val serverConfig by viewModel.serverConfig.collectAsState()
  val serverLogs by viewModel.serverLogs.collectAsState()
  val cpuUsage by viewModel.serverCpuUsage.collectAsState()
  val ramUsageMb by viewModel.serverRamUsageMb.collectAsState()
  val tps by viewModel.serverTps.collectAsState()
  val deviceHardware by viewModel.deviceHardware.collectAsState()
  val localIp by viewModel.localIp.collectAsState()
  val availableVersions = viewModel.availableVersions

  var selectedTab by remember { mutableIntStateOf(0) } // 0 = Console & Telemetry, 1 = Hardware & Settings
  var commandText by remember { mutableStateOf("") }
  val consoleListState = rememberLazyListState()

  // Auto-scroll console to bottom when new logs arrive
  LaunchedEffect(serverLogs.size) {
    if (serverLogs.isNotEmpty()) {
      consoleListState.animateScrollToItem(serverLogs.size - 1)
    }
  }

  // Local settings draft state
  var draftType by remember(serverConfig) { mutableStateOf(serverConfig.serverType) }
  var draftVersion by remember(serverConfig) { mutableStateOf(serverConfig.gameVersion) }
  var draftPort by remember(serverConfig) { mutableStateOf(serverConfig.port.toString()) }
  var draftMaxRam by remember(serverConfig) { mutableFloatStateOf(serverConfig.maxRamMb.toFloat()) }
  var draftCpuThreads by remember(serverConfig) { mutableFloatStateOf(serverConfig.cpuThreads.toFloat()) }
  var draftOnlineMode by remember(serverConfig) { mutableStateOf(serverConfig.onlineMode) }
  var draftMaxPlayers by remember(serverConfig) { mutableFloatStateOf(serverConfig.maxPlayers.toFloat()) }
  var draftMotd by remember(serverConfig) { mutableStateOf(serverConfig.motd) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(IronDarkBg)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
      Spacer(modifier = Modifier.height(8.dp))

      // Header: Local Server Title & Live IP
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "LOCAL DEDICATED SERVER",
            color = IronCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Text(
            text = "Host On Android CPU & RAM",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }

        // Live Server Status Badge
        val (statusColor, statusLabel) = when (serverStatus) {
          ServerStatus.RUNNING -> EmeraldOnline to "ONLINE"
          ServerStatus.STARTING -> ForgeAmber to "STARTING..."
          ServerStatus.STOPPING -> ForgeAmber to "STOPPING..."
          ServerStatus.OFFLINE -> RedstoneOffline to "OFFLINE"
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(statusColor.copy(alpha = 0.15f))
            .border(1.dp, statusColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Circle,
              contentDescription = null,
              tint = statusColor,
              modifier = Modifier.size(8.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = statusLabel,
              color = statusColor,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // LAN Connection Info Banner (Click to copy)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, IronSurfaceBorder, RoundedCornerShape(12.dp))
          .clickable {
            val ipString = "$localIp:${serverConfig.port}"
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Server IP", ipString))
            Toast.makeText(context, "Copied IP: $ipString", Toast.LENGTH_SHORT).show()
          },
        colors = CardDefaults.cardColors(containerColor = IronSurface),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Wifi,
              contentDescription = null,
              tint = IronCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "LAN SERVER IP (Direct Connect)",
                color = Color(0xFF8B949E),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "$localIp:${serverConfig.port}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Tap to Copy",
              color = IronCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy IP",
              tint = IronCyan,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Server Power Controls
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val isBusy = serverStatus == ServerStatus.STARTING || serverStatus == ServerStatus.STOPPING
        if (serverStatus == ServerStatus.RUNNING) {
          Button(
            onClick = { viewModel.stopServer() },
            enabled = !isBusy,
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("server_stop_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = RedstoneOffline,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Stop Server", fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = { viewModel.restartServer() },
            enabled = !isBusy,
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("server_restart_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = ForgeOrange,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Restart", fontWeight = FontWeight.Bold)
          }
        } else {
          Button(
            onClick = { viewModel.startServer() },
            enabled = !isBusy,
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("server_start_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = EmeraldOnline,
              contentColor = Color(0xFF003814)
            ),
            shape = RoundedCornerShape(10.dp)
          ) {
            if (isBusy) {
              CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = Color.White,
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Starting Server...", fontWeight = FontWeight.Bold)
            } else {
              Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Start ${serverConfig.serverType.displayName} Server", fontWeight = FontWeight.Black, fontSize = 15.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Tab switcher: Console / Terminal VS Hardware & Tuning
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = IronSurface,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = IronCyan
          )
        },
        divider = {
          Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(IronSurfaceBorder))
        }
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Terminal, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Console & Telemetry", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          },
          selectedContentColor = IronCyan,
          unselectedContentColor = Color(0xFF8B949E)
        )

        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("CPU/RAM Tuning & Software", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          },
          selectedContentColor = IronCyan,
          unselectedContentColor = Color(0xFF8B949E)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // TAB 0: Console & Telemetry
      if (selectedTab == 0) {
        // Live Telemetry Bar (CPU, RAM, TPS)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // CPU Gauge
          Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = IronSurface),
            shape = RoundedCornerShape(8.dp)
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              Text("CPU LOAD", color = Color(0xFF8B949E), fontSize = 9.sp, fontWeight = FontWeight.Bold)
              Text(
                text = "${"%.1f".format(cpuUsage)}%",
                color = if (cpuUsage > 80f) RedstoneOffline else IronCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              LinearProgressIndicator(
                progress = { (cpuUsage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)),
                color = IronCyan,
                trackColor = IronSurfaceVariant
              )
            }
          }

          // RAM Gauge
          Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = IronSurface),
            shape = RoundedCornerShape(8.dp)
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              Text("SERVER RAM", color = Color(0xFF8B949E), fontSize = 9.sp, fontWeight = FontWeight.Bold)
              Text(
                text = "$ramUsageMb / ${serverConfig.maxRamMb}M",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              val ramFraction = if (serverConfig.maxRamMb > 0) (ramUsageMb.toFloat() / serverConfig.maxRamMb).coerceIn(0f, 1f) else 0f
              LinearProgressIndicator(
                progress = { ramFraction },
                modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)),
                color = ForgeAmber,
                trackColor = IronSurfaceVariant
              )
            }
          }

          // TPS Gauge
          Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = IronSurface),
            shape = RoundedCornerShape(8.dp)
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              Text("TICK RATE", color = Color(0xFF8B949E), fontSize = 9.sp, fontWeight = FontWeight.Bold)
              Text(
                text = "${"%.1f".format(tps)} TPS",
                color = EmeraldOnline,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text("Target 20.0", color = Color(0xFF8B949E), fontSize = 9.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Console Window (Terminal Box)
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF090D13))
            .border(1.dp, Color(0xFF30363D), RoundedCornerShape(10.dp))
            .padding(8.dp)
        ) {
          LazyColumn(
            state = consoleListState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            items(serverLogs, key = { it.id }) { log ->
              val (color, prefix) = when (log.level) {
                "SUCCESS" -> EmeraldOnline to "[SUCCESS]"
                "WARN" -> ForgeAmber to "[WARN]"
                "ERROR" -> RedstoneOffline to "[ERROR]"
                "CHAT" -> FabricPurple to "[CHAT]"
                "INPUT" -> IronCyan to ">"
                else -> Color(0xFF8B949E) to "[INFO]"
              }
              Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                  text = "[${log.timestamp}] ",
                  color = Color(0xFF484F58),
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace
                )
                Text(
                  text = "$prefix ",
                  color = color,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace
                )
                Text(
                  text = log.message,
                  color = Color(0xFFE6EDF3),
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Quick Command Action Chips
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          listOf("/help", "/tps", "/list", "/gamemode creative", "/time set day", "/say Server Online!").forEach { cmd ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(IronSurfaceVariant)
                .clickable { viewModel.executeServerCommand(cmd) }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = cmd,
                color = IronCyan,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Terminal Command Input Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = commandText,
            onValueChange = { commandText = it },
            placeholder = { Text("Type server command (e.g. /op, /gamemode)...", fontSize = 12.sp) },
            modifier = Modifier
              .weight(1f)
              .testTag("server_command_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = IronCyan,
              unfocusedBorderColor = IronSurfaceBorder,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedContainerColor = IronSurface,
              unfocusedContainerColor = IronSurface
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
              onSend = {
                if (commandText.isNotBlank()) {
                  viewModel.executeServerCommand(commandText)
                  commandText = ""
                }
              }
            )
          )

          Spacer(modifier = Modifier.width(6.dp))

          IconButton(
            onClick = {
              if (commandText.isNotBlank()) {
                viewModel.executeServerCommand(commandText)
                commandText = ""
              }
            },
            modifier = Modifier
              .size(48.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF00363D))
              .testTag("server_send_command_button")
          ) {
            Icon(
              imageVector = Icons.Default.Send,
              contentDescription = "Send Command",
              tint = IronCyan,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          IconButton(
            onClick = { viewModel.clearServerLogs() },
            modifier = Modifier.size(40.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = "Clear Logs",
              tint = Color(0xFF8B949E),
              modifier = Modifier.size(18.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      // TAB 1: Hardware & Settings Tuning ("ปรับแต่งได้ตามต้องการ")
      if (selectedTab == 1) {
        LazyColumn(
          modifier = Modifier.weight(1f).fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // 1. Server Software Type Selection
          item {
            Card(
              modifier = Modifier.fillMaxWidth().border(1.dp, IronSurfaceBorder, RoundedCornerShape(12.dp)),
              colors = CardDefaults.cardColors(containerColor = IronSurface),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "SERVER SOFTWARE (เลือกซอฟต์แวร์)",
                  color = IronCyan,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                  verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  ServerType.values().forEach { st ->
                    val isSelected = draftType == st
                    val color = Color(st.badgeColorHex)
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) color.copy(alpha = 0.25f) else IronSurfaceVariant)
                        .border(1.dp, if (isSelected) color else IronSurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { draftType = st }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                      Text(
                        text = st.displayName,
                        color = if (isSelected) color else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = draftType.description,
                  color = Color(0xFFBAC7D5),
                  fontSize = 11.sp
                )
              }
            }
          }

          // 2. Game Version Selection
          item {
            Card(
              modifier = Modifier.fillMaxWidth().border(1.dp, IronSurfaceBorder, RoundedCornerShape(12.dp)),
              colors = CardDefaults.cardColors(containerColor = IronSurface),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "GAME VERSION (เช่น 26.3, 26.2, 1.21.4)",
                  color = IronCyan,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                  verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  listOf("26.3", "26.2", "1.21.4", "1.20.4", "1.19.4", "1.18.2", "1.16.5").forEach { ver ->
                    val isSelected = draftVersion == ver
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) Color(0xFF00363D) else IronSurfaceVariant)
                        .border(1.dp, if (isSelected) IronCyan else IronSurfaceBorder, RoundedCornerShape(6.dp))
                        .clickable { draftVersion = ver }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                      Text(
                        text = ver,
                        color = if (isSelected) IronCyan else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = draftVersion,
                  onValueChange = { draftVersion = it },
                  label = { Text("Custom Version") },
                  modifier = Modifier.fillMaxWidth(),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IronCyan,
                    unfocusedBorderColor = IronSurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                  ),
                  singleLine = true
                )
              }
            }
          }

          // 3. RAM & CPU Device Tuning
          item {
            Card(
              modifier = Modifier.fillMaxWidth().border(1.dp, IronSurfaceBorder, RoundedCornerShape(12.dp)),
              colors = CardDefaults.cardColors(containerColor = IronSurface),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "RAM & CPU TUNING (ทรัพยากรเครื่อง)",
                    color = IronCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Total Phone RAM: ${deviceHardware.totalRamMb} MB",
                    color = Color(0xFF8B949E),
                    fontSize = 10.sp
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Max RAM Slider
                Text(
                  text = "Allocated Server Heap (-Xmx): ${draftMaxRam.toInt()} MB (${"%.1f".format(draftMaxRam / 1024f)} GB)",
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                Slider(
                  value = draftMaxRam,
                  onValueChange = { draftMaxRam = it },
                  valueRange = 1024f..(deviceHardware.totalRamMb.coerceAtLeast(4096).toFloat()),
                  steps = 5,
                  colors = SliderDefaults.colors(
                    thumbColor = IronCyan,
                    activeTrackColor = IronCyan,
                    inactiveTrackColor = IronSurfaceVariant
                  )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // CPU Threads Slider
                Text(
                  text = "GC & Worker Threads: ${draftCpuThreads.toInt()} Threads (${deviceHardware.cpuCores} Cores Available)",
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                Slider(
                  value = draftCpuThreads,
                  onValueChange = { draftCpuThreads = it },
                  valueRange = 1f..deviceHardware.cpuCores.toFloat(),
                  steps = (deviceHardware.cpuCores - 2).coerceAtLeast(0),
                  colors = SliderDefaults.colors(
                    thumbColor = ForgeAmber,
                    activeTrackColor = ForgeAmber,
                    inactiveTrackColor = IronSurfaceVariant
                  )
                )
              }
            }
          }

          // 4. Server Properties Config
          item {
            Card(
              modifier = Modifier.fillMaxWidth().border(1.dp, IronSurfaceBorder, RoundedCornerShape(12.dp)),
              colors = CardDefaults.cardColors(containerColor = IronSurface),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "SERVER.PROPERTIES SETTINGS",
                  color = IronCyan,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                  value = draftPort,
                  onValueChange = { draftPort = it },
                  label = { Text("Server Port") },
                  modifier = Modifier.fillMaxWidth(),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IronCyan,
                    unfocusedBorderColor = IronSurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                  ),
                  singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                  value = draftMotd,
                  onValueChange = { draftMotd = it },
                  label = { Text("MOTD (Message of the Day)") },
                  modifier = Modifier.fillMaxWidth(),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IronCyan,
                    unfocusedBorderColor = IronSurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                  ),
                  singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Online Mode Switch (Offline / Cracked LAN toggle)
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text("Online Mode (Mojang Auth)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Turn OFF to allow LAN & offline accounts", color = Color(0xFF8B949E), fontSize = 11.sp)
                  }
                  Switch(
                    checked = draftOnlineMode,
                    onCheckedChange = { draftOnlineMode = it },
                    colors = SwitchDefaults.colors(
                      checkedThumbColor = IronCyan,
                      checkedTrackColor = Color(0xFF00363D)
                    )
                  )
                }
              }
            }
          }

          // Apply & Save Button
          item {
            Button(
              onClick = {
                val portNum = draftPort.toIntOrNull() ?: 25565
                viewModel.updateServerSettings(
                  serverType = draftType,
                  version = draftVersion,
                  port = portNum,
                  minRam = (draftMaxRam / 2).toInt().coerceAtLeast(512),
                  maxRam = draftMaxRam.toInt(),
                  cpuThreads = draftCpuThreads.toInt(),
                  onlineMode = draftOnlineMode,
                  maxPlayers = draftMaxPlayers.toInt(),
                  motd = draftMotd,
                  viewDist = 10
                )
                Toast.makeText(context, "Server configuration updated!", Toast.LENGTH_SHORT).show()
                selectedTab = 0
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("save_server_config_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = IronCyan,
                contentColor = Color(0xFF00363D)
              ),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Save & Apply Configuration", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
          }
        }
      }
    }
  }
}
