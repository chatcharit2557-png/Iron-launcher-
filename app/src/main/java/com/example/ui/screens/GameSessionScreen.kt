package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModLoader
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.IronCyan
import com.example.ui.theme.IronSurface
import com.example.ui.theme.IronSurfaceBorder
import com.example.ui.theme.RedstoneOffline
import com.example.ui.viewmodel.LauncherViewModel

@Composable
fun GameSessionScreen(viewModel: LauncherViewModel) {
  val context = LocalContext.current
  val activeProfile by viewModel.activeProfile.collectAsState()
  val serverStatus by viewModel.serverStatus.collectAsState()
  val serverConfig by viewModel.serverConfig.collectAsState()

  var showF3Debug by remember { mutableStateOf(true) }
  var showPauseMenu by remember { mutableStateOf(false) }
  var showChatDialog by remember { mutableStateOf(false) }
  var selectedHotbarSlot by remember { mutableIntStateOf(0) }
  var playerActionMessage by remember { mutableStateOf("Game running") }
  var isConnectedToLocalServer by remember { mutableStateOf(false) }

  // Virtual player coords
  var posX by remember { mutableFloatStateOf(142.45f) }
  var posY by remember { mutableFloatStateOf(68.0f) }
  var posZ by remember { mutableFloatStateOf(-210.83f) }
  var isSneaking by remember { mutableStateOf(false) }

  BackHandler {
    showPauseMenu = true
  }

  val hotbarItems = listOf(
    "🗡️ Diamond Sword",
    "⛏️ Iron Pickaxe",
    "🪵 Oak Wood (64)",
    "🕯️ Torches (32)",
    "🍎 Golden Apple (8)",
    "🥩 Cooked Beef (16)",
    "🏹 Bow",
    "🪣 Water Bucket",
    "🔴 Redstone (48)"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF1E2836))
  ) {
    // 1. Simulated 3D Minecraft Environment Canvas
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectDragGestures { _, dragAmount ->
            posX += dragAmount.x * 0.05f
            posZ += dragAmount.y * 0.05f
          }
        }
    ) {
      val w = size.width
      val h = size.height

      // Sky gradient
      drawRect(
        color = Color(0xFF6BA6FE),
        topLeft = Offset(0f, 0f),
        size = Size(w, h * 0.55f)
      )

      // Sun
      drawRect(
        color = Color(0xFFFFFBDB),
        topLeft = Offset(w * 0.72f, h * 0.15f),
        size = Size(50f, 50f)
      )

      // Mountains / Horizon
      drawRect(
        color = Color(0xFF4A6B52),
        topLeft = Offset(0f, h * 0.48f),
        size = Size(w, h * 0.08f)
      )

      // Grassy Ground
      drawRect(
        color = Color(0xFF4C8C38),
        topLeft = Offset(0f, h * 0.55f),
        size = Size(w, h * 0.45f)
      )

      // Isometric Block Outlines in World
      drawRect(
        color = Color(0xFF386828),
        topLeft = Offset(w * 0.4f, h * 0.58f),
        size = Size(100f, 80f)
      )
      drawRect(
        color = Color(0xFF7D603A),
        topLeft = Offset(w * 0.4f, h * 0.66f),
        size = Size(100f, 120f)
      )

      // Center Crosshair (+)
      val cx = w / 2
      val cy = h / 2
      val cLen = 14f
      drawLine(Color(0xCCFFFFFF), Offset(cx - cLen, cy), Offset(cx + cLen, cy), strokeWidth = 3f)
      drawLine(Color(0xCCFFFFFF), Offset(cx, cy - cLen), Offset(cx, cy + cLen), strokeWidth = 3f)
    }

    // 2. F3 Debug Screen Overlay (Authentic Minecraft Java F3)
    if (showF3Debug) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 36.dp, start = 12.dp, end = 12.dp)
      ) {
        Column(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0x99000000))
            .padding(6.dp)
        ) {
          Text(
            text = "Minecraft ${activeProfile?.gameVersion ?: "1.21.4"} (${activeProfile?.modLoader?.displayName ?: "Fabric"})",
            color = Color.White,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "60.0 fps (V-Sync, OpenGL ES 3.2)",
            color = Color.White,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "XYZ: ${"%.3f".format(posX)} / ${"%.3f".format(posY)} / ${"%.3f".format(posZ)}",
            color = Color.White,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "Block: ${posX.toInt()} ${posY.toInt()} ${posZ.toInt()} [Facing: North]",
            color = Color.White,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "Biome: minecraft:plains | Light: 15 (15 sky, 0 block)",
            color = Color.White,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "Mem: 46% 1410/${activeProfile?.allocatedRamMb ?: 3072}MB (G1GC)",
            color = IronCyan,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
          if (isConnectedToLocalServer) {
            Text(
              text = "Connected: Local Server (0.0.0.0:${serverConfig.port}) 12ms",
              color = EmeraldOnline,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // 3. Top Action Bar: F3, Chat, Server Connect, Pause Menu
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, start = 12.dp, end = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        // F3 Toggle Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (showF3Debug) Color(0xFF00363D) else Color(0x80000000))
            .border(1.dp, if (showF3Debug) IronCyan else Color(0x55FFFFFF), RoundedCornerShape(6.dp))
            .clickable { showF3Debug = !showF3Debug }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text("F3", color = if (showF3Debug) IronCyan else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        // Chat Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0x80000000))
            .border(1.dp, Color(0x55FFFFFF), RoundedCornerShape(6.dp))
            .clickable { showChatDialog = true }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text("CHAT", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        // Local Server Direct Connect
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isConnectedToLocalServer) Color(0xFF00363D) else Color(0x80000000))
            .border(1.dp, if (isConnectedToLocalServer) EmeraldOnline else Color(0x55FFFFFF), RoundedCornerShape(6.dp))
            .clickable {
              if (isConnectedToLocalServer) {
                isConnectedToLocalServer = false
                playerActionMessage = "Disconnected from local server"
              } else {
                isConnectedToLocalServer = true
                playerActionMessage = "Connected to Local Server [127.0.0.1:${serverConfig.port}]"
              }
              Toast.makeText(context, playerActionMessage, Toast.LENGTH_SHORT).show()
            }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Dns,
              contentDescription = null,
              tint = if (isConnectedToLocalServer) EmeraldOnline else Color.White,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isConnectedToLocalServer) "SRV CONNECTED" else "JOIN LOCAL SRV",
              color = if (isConnectedToLocalServer) EmeraldOnline else Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Pause Menu Button
      IconButton(
        onClick = { showPauseMenu = true },
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0x80000000))
          .border(1.dp, Color(0x55FFFFFF), RoundedCornerShape(6.dp))
      ) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Pause Game",
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
      }
    }

    // 4. Virtual D-Pad (Left Side)
    Box(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(start = 16.dp, bottom = 90.dp)
        .size(130.dp)
    ) {
      // Forward (W)
      Box(
        modifier = Modifier
          .size(42.dp)
          .align(Alignment.TopCenter)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0x8821262D))
          .border(1.dp, Color(0x88FFFFFF), RoundedCornerShape(8.dp))
          .clickable {
            posZ -= 1.0f
            playerActionMessage = "Walking forward"
          },
        contentAlignment = Alignment.Center
      ) {
        Text("▲", color = Color.White, fontSize = 16.sp)
      }

      // Backward (S)
      Box(
        modifier = Modifier
          .size(42.dp)
          .align(Alignment.BottomCenter)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0x8821262D))
          .border(1.dp, Color(0x88FFFFFF), RoundedCornerShape(8.dp))
          .clickable {
            posZ += 1.0f
            playerActionMessage = "Walking back"
          },
        contentAlignment = Alignment.Center
      ) {
        Text("▼", color = Color.White, fontSize = 16.sp)
      }

      // Left (A)
      Box(
        modifier = Modifier
          .size(42.dp)
          .align(Alignment.CenterStart)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0x8821262D))
          .border(1.dp, Color(0x88FFFFFF), RoundedCornerShape(8.dp))
          .clickable {
            posX -= 1.0f
            playerActionMessage = "Strafing left"
          },
        contentAlignment = Alignment.Center
      ) {
        Text("◀", color = Color.White, fontSize = 16.sp)
      }

      // Right (D)
      Box(
        modifier = Modifier
          .size(42.dp)
          .align(Alignment.CenterEnd)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0x8821262D))
          .border(1.dp, Color(0x88FFFFFF), RoundedCornerShape(8.dp))
          .clickable {
            posX += 1.0f
            playerActionMessage = "Strafing right"
          },
        contentAlignment = Alignment.Center
      ) {
        Text("▶", color = Color.White, fontSize = 16.sp)
      }

      // Center Sneak Toggle
      Box(
        modifier = Modifier
          .size(36.dp)
          .align(Alignment.Center)
          .clip(CircleShape)
          .background(if (isSneaking) IronCyan else Color(0xAA00363D))
          .border(1.dp, Color.White, CircleShape)
          .clickable {
            isSneaking = !isSneaking
            playerActionMessage = if (isSneaking) "Sneaking (Crouched)" else "Standing"
          },
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "SNK",
          color = if (isSneaking) Color(0xFF00363D) else Color.White,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // 5. Right Action Buttons: Attack, Use, Jump
    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 90.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalAlignment = Alignment.End
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        // Place / Use Button
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color(0xAA00363D))
            .border(1.5.dp, IronCyan, CircleShape)
            .clickable {
              playerActionMessage = "Used: ${hotbarItems[selectedHotbarSlot]}"
              Toast.makeText(context, playerActionMessage, Toast.LENGTH_SHORT).show()
            },
          contentAlignment = Alignment.Center
        ) {
          Text("USE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // Attack Button
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color(0xAA451214))
            .border(1.5.dp, RedstoneOffline, CircleShape)
            .clickable {
              playerActionMessage = "Attacked with: ${hotbarItems[selectedHotbarSlot]}"
              Toast.makeText(context, playerActionMessage, Toast.LENGTH_SHORT).show()
            },
          contentAlignment = Alignment.Center
        ) {
          Text("ATT", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }

      // Big Jump Button
      Box(
        modifier = Modifier
          .size(56.dp)
          .clip(CircleShape)
          .background(Color(0xAA21262D))
          .border(1.5.dp, Color.White, CircleShape)
          .clickable {
            posY += 1.2f
            playerActionMessage = "Jumped!"
          },
        contentAlignment = Alignment.Center
      ) {
        Text("JUMP", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black)
      }
    }

    // 6. Bottom HUD: Hearts, Hunger, XP bar, 9-Slot Hotbar
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Hearts & Hunger Row
      Row(
        modifier = Modifier.width(320.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // 10 Hearts
        Text(
          text = "❤️❤️❤️❤️❤️❤️❤️❤️❤️❤️",
          fontSize = 11.sp
        )
        // 10 Hunger drumsticks
        Text(
          text = "🍗🍗🍗🍗🍗🍗🍗🍗🍗🍗",
          fontSize = 11.sp
        )
      }

      // XP Bar
      Box(
        modifier = Modifier
          .width(320.dp)
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(Color(0xFF222222))
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(0.72f)
            .height(6.dp)
            .background(Color(0xFF00E676))
        )
      }
      Text(
        text = "34",
        color = Color(0xFF00E676),
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp
      )

      Spacer(modifier = Modifier.height(2.dp))

      // 9-Slot Hotbar
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0xCC111111))
          .border(1.5.dp, Color(0xFF555555), RoundedCornerShape(6.dp))
          .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
      ) {
        hotbarItems.forEachIndexed { index, item ->
          val isSelected = selectedHotbarSlot == index
          val iconEmoji = item.split(" ").first()
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(if (isSelected) Color(0x8800E5FF) else Color(0x66222222))
              .border(
                1.5.dp,
                if (isSelected) Color.White else Color(0x33FFFFFF),
                RoundedCornerShape(4.dp)
              )
              .clickable {
                selectedHotbarSlot = index
                playerActionMessage = "Equipped: $item"
              },
            contentAlignment = Alignment.Center
          ) {
            Text(text = iconEmoji, fontSize = 16.sp)
          }
        }
      }
    }

    // Pause / Escape Menu Dialog
    if (showPauseMenu) {
      AlertDialog(
        onDismissRequest = { showPauseMenu = false },
        containerColor = IronSurface,
        title = {
          Text(
            text = "Game Paused",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = "Profile: ${activeProfile?.name}",
              color = IronCyan,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Version: ${activeProfile?.gameVersion} (${activeProfile?.modLoader})",
              color = Color(0xFFBAC7D5),
              fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
              onClick = { showPauseMenu = false },
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = IronCyan, contentColor = Color(0xFF00363D))
            ) {
              Text("Back to Game", fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = {
                isConnectedToLocalServer = true
                showPauseMenu = false
                Toast.makeText(context, "Connected to Local Server 127.0.0.1:${serverConfig.port}", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF21262D), contentColor = Color.White)
            ) {
              Text("Connect to Local Server", fontWeight = FontWeight.SemiBold)
            }

            Button(
              onClick = {
                showPauseMenu = false
                viewModel.exitGameSession()
              },
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = RedstoneOffline, contentColor = Color.White)
            ) {
              Text("Save and Quit to Launcher", fontWeight = FontWeight.Bold)
            }
          }
        },
        confirmButton = {}
      )
    }

    // In-Game Chat Dialog
    if (showChatDialog) {
      var chatMsg by remember { mutableStateOf("") }
      AlertDialog(
        onDismissRequest = { showChatDialog = false },
        containerColor = IronSurface,
        title = { Text("Minecraft In-Game Chat", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
          Column {
            Text("Send chat or command in-game:", color = Color(0xFF8B949E), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = chatMsg,
              onValueChange = { chatMsg = it },
              placeholder = { Text("e.g. Hello world or /gamemode c") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              if (chatMsg.isNotBlank()) {
                if (chatMsg.startsWith("/")) {
                  viewModel.executeServerCommand(chatMsg)
                }
                playerActionMessage = "Chat sent: $chatMsg"
                Toast.makeText(context, playerActionMessage, Toast.LENGTH_SHORT).show()
              }
              showChatDialog = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = IronCyan, contentColor = Color(0xFF00363D))
          ) {
            Text("Send")
          }
        },
        dismissButton = {
          TextButton(onClick = { showChatDialog = false }) {
            Text("Cancel", color = Color(0xFF8B949E))
          }
        }
      )
    }
  }
}
