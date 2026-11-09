package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerAccount
import com.example.data.model.ServerStatus
import com.example.data.service.DeviceHardwareInfo
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.IronCyan
import com.example.ui.theme.IronSurface
import com.example.ui.theme.IronSurfaceBorder
import com.example.ui.theme.RedstoneOffline

@Composable
fun AppTopBar(
  playerAccount: PlayerAccount,
  serverStatus: ServerStatus,
  deviceHardware: DeviceHardwareInfo,
  onAccountClick: () -> Unit,
  onServerClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(IronSurface)
      .border(1.dp, IronSurfaceBorder)
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // App Branding
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF00363D))
            .border(1.5.dp, IronCyan, RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "IL",
            color = IronCyan,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "IRON",
              color = IronCyan,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 17.sp,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "LAUNCHER",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              letterSpacing = 1.sp
            )
          }
          Text(
            text = "Minecraft Java & Local Host",
            color = Color(0xFF8B949E),
            fontSize = 11.sp
          )
        }
      }

      // Status Indicators & Account Profile
      Row(verticalAlignment = Alignment.CenterVertically) {
        // Server quick badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF21262D))
            .border(1.dp, IronSurfaceBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onServerClick)
            .testTag("top_bar_server_chip")
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            val statusColor = when (serverStatus) {
              ServerStatus.RUNNING -> EmeraldOnline
              ServerStatus.STARTING, ServerStatus.STOPPING -> ForgeAmber
              ServerStatus.OFFLINE -> RedstoneOffline
            }
            Icon(
              imageVector = Icons.Default.Circle,
              contentDescription = "Server Status",
              tint = statusColor,
              modifier = Modifier.size(8.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (serverStatus == ServerStatus.RUNNING) "SRV ON" else "SERVER",
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Player Account Chip
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF004F58))
            .border(1.dp, IronCyan, RoundedCornerShape(20.dp))
            .clickable(onClick = onAccountClick)
            .testTag("top_bar_account_button")
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AccountCircle,
              contentDescription = "Account",
              tint = IronCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = playerAccount.username,
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
