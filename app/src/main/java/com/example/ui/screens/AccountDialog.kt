package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.IronCyan
import com.example.ui.theme.IronSurface
import com.example.ui.theme.IronSurfaceBorder
import com.example.ui.theme.IronSurfaceVariant
import com.example.ui.viewmodel.LauncherViewModel

@Composable
fun AccountDialog(
  viewModel: LauncherViewModel,
  onDismiss: () -> Unit
) {
  val playerAccount by viewModel.playerAccount.collectAsState()

  var username by remember { mutableStateOf(playerAccount.username) }
  var selectedSkin by remember { mutableStateOf(playerAccount.skinName) }
  var selectedCape by remember { mutableStateOf(playerAccount.capeName) }

  val skins = listOf("Iron Knight", "Classic Steve", "Modern Alex", "Cyber Enderman")
  val capes = listOf("Founder Cyan", "Minecon 2011", "Mojang Migrator", "None")

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = IronSurface,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.AccountCircle,
          contentDescription = null,
          tint = IronCyan,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Player & Account Settings",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = username,
          onValueChange = { username = it },
          label = { Text("Player IGN (Minecraft Username)") },
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IronCyan,
            unfocusedBorderColor = IronSurfaceBorder,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          singleLine = true
        )

        Text(
          text = "Select Player Skin",
          color = IronCyan,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          skins.forEach { skin ->
            val isSelected = selectedSkin == skin
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFF00363D) else IronSurfaceVariant)
                .border(1.dp, if (isSelected) IronCyan else IronSurfaceBorder, RoundedCornerShape(8.dp))
                .clickable { selectedSkin = skin }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = when (skin) {
                    "Iron Knight" -> "🛡️"
                    "Classic Steve" -> "🧑"
                    "Modern Alex" -> "👩"
                    else -> "👾"
                  },
                  fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = skin.split(" ").last(),
                  color = if (isSelected) IronCyan else Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }

        Text(
          text = "Cosmetic Cape",
          color = IronCyan,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          capes.forEach { cape ->
            val isSelected = selectedCape == cape
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSelected) Color(0xFF00363D) else IronSurfaceVariant)
                .border(1.dp, if (isSelected) IronCyan else IronSurfaceBorder, RoundedCornerShape(6.dp))
                .clickable { selectedCape = cape }
                .padding(vertical = 6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = cape.split(" ").first(),
                color = if (isSelected) IronCyan else Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          viewModel.updateAccount(username, selectedSkin, selectedCape)
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = IronCyan, contentColor = Color(0xFF00363D))
      ) {
        Text("Save Account", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = Color(0xFF8B949E))
      }
    }
  )
}
