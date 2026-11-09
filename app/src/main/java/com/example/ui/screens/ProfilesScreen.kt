package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameProfile
import com.example.data.model.ModLoader
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.FabricPurple
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.IronCyan
import com.example.ui.theme.IronDarkBg
import com.example.ui.theme.IronSurface
import com.example.ui.theme.IronSurfaceBorder
import com.example.ui.theme.IronSurfaceVariant
import com.example.ui.theme.RedstoneOffline
import com.example.ui.viewmodel.LauncherViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfilesScreen(viewModel: LauncherViewModel) {
  val profiles by viewModel.profiles.collectAsState()
  val selectedProfileId by viewModel.selectedProfileId.collectAsState()
  val availableVersions = viewModel.availableVersions

  var editingProfile by remember { mutableStateOf<GameProfile?>(null) }
  var showCreateDialog by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(IronDarkBg)
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "PROFILES & VERSIONS",
              color = IronCyan,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Text(
              text = "Manage Minecraft Installations",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
          Text(
            text = "${profiles.size} Profiles",
            color = Color(0xFF8B949E),
            fontSize = 12.sp
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
      }

      items(profiles, key = { it.id }) { profile ->
        val isSelected = profile.id == selectedProfileId
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) IronCyan else IronSurfaceBorder,
              shape = RoundedCornerShape(14.dp)
            )
            .clickable { viewModel.selectProfile(profile.id) },
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
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color(0xFF00363D) else IronSurfaceVariant)
                    .border(
                      1.dp,
                      if (isSelected) IronCyan else IronSurfaceBorder,
                      RoundedCornerShape(8.dp)
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = null,
                    tint = if (isSelected) IronCyan else Color(0xFF8B949E),
                    modifier = Modifier.size(20.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = profile.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Java Runtime • ${profile.allocatedRamMb} MB Heap",
                    color = Color(0xFF8B949E),
                    fontSize = 11.sp
                  )
                }
              }

              // Active badge or select button
              if (isSelected) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF00363D))
                    .border(1.dp, IronCyan, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Selected",
                      tint = IronCyan,
                      modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "ACTIVE",
                      color = IronCyan,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              } else {
                TextButton(
                  onClick = { viewModel.selectProfile(profile.id) },
                  colors = ButtonDefaults.textButtonColors(contentColor = IronCyan)
                ) {
                  Text("Set Active", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              // Game Version
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color(0xFF21262D))
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = "MC ${profile.gameVersion}",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }

              // ModLoader
              val loaderColor = Color(profile.modLoader.badgeColorHex)
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(loaderColor.copy(alpha = 0.2f))
                  .border(1.dp, loaderColor, RoundedCornerShape(6.dp))
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = profile.modLoader.displayName,
                  color = loaderColor,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              // Fabric API status
              if (profile.modLoader == ModLoader.FABRIC) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                      if (profile.fabricApiInstalled) Color(0xFF00363D) else Color(0xFF381E22)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = if (profile.fabricApiInstalled) "Fabric API ✓" else "No Fabric API",
                    color = if (profile.fabricApiInstalled) IronCyan else RedstoneOffline,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action row (Edit / Delete)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End,
              verticalAlignment = Alignment.CenterVertically
            ) {
              TextButton(
                onClick = { editingProfile = profile },
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFBAC7D5))
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Edit Profile",
                  modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Edit Config", fontSize = 12.sp)
              }

              if (profiles.size > 1) {
                TextButton(
                  onClick = { viewModel.deleteProfile(profile.id) },
                  colors = ButtonDefaults.textButtonColors(contentColor = RedstoneOffline)
                ) {
                  Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Profile",
                    modifier = Modifier.size(15.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Delete", fontSize = 12.sp)
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }

    // Add Profile FAB
    FloatingActionButton(
      onClick = { showCreateDialog = true },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("create_profile_fab"),
      containerColor = IronCyan,
      contentColor = Color(0xFF00363D),
      shape = CircleShape
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Create Profile",
        modifier = Modifier.size(24.dp)
      )
    }

    // Create / Edit Profile Dialog
    if (showCreateDialog || editingProfile != null) {
      val isEditing = editingProfile != null
      val initial = editingProfile

      var name by remember { mutableStateOf(initial?.name ?: "") }
      var version by remember { mutableStateOf(initial?.gameVersion ?: "1.21.4") }
      var modLoader by remember { mutableStateOf(initial?.modLoader ?: ModLoader.FABRIC) }
      var fabricApi by remember { mutableStateOf(initial?.fabricApiInstalled ?: true) }
      var ramMb by remember { mutableFloatStateOf((initial?.allocatedRamMb ?: 3072).toFloat()) }
      var jvmArgs by remember {
        mutableStateOf(
          initial?.jvmArgs ?: "-XX:+UseG1GC -Dsun.rmi.dgc.server.gcInterval=2147483646"
        )
      }

      AlertDialog(
        onDismissRequest = {
          showCreateDialog = false
          editingProfile = null
        },
        containerColor = IronSurface,
        title = {
          Text(
            text = if (isEditing) "Edit Game Profile" else "Create New Profile",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        text = {
          LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            item {
              OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Profile Name") },
                placeholder = { Text("e.g. My Survival 1.21.4") },
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

            item {
              Text(
                text = "Minecraft Game Version",
                color = IronCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              // Quick selection chips
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
              ) {
                availableVersions.forEach { ver ->
                  val isVerSelected = version == ver
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(if (isVerSelected) Color(0xFF00363D) else IronSurfaceVariant)
                      .border(
                        1.dp,
                        if (isVerSelected) IronCyan else IronSurfaceBorder,
                        RoundedCornerShape(6.dp)
                      )
                      .clickable { version = ver }
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = ver,
                      color = if (isVerSelected) IronCyan else Color.White,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
              // Custom Version text field
              OutlinedTextField(
                value = version,
                onValueChange = { version = it },
                label = { Text("Selected / Custom Version") },
                placeholder = { Text("e.g. 26.3, 26.2, 1.21.4") },
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

            item {
              Text(
                text = "Mod Loader",
                color = IronCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
              ) {
                ModLoader.values().forEach { loader ->
                  val isLoaderSelected = modLoader == loader
                  val color = Color(loader.badgeColorHex)
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(if (isLoaderSelected) color.copy(alpha = 0.25f) else IronSurfaceVariant)
                      .border(
                        1.dp,
                        if (isLoaderSelected) color else IronSurfaceBorder,
                        RoundedCornerShape(6.dp)
                      )
                      .clickable { modLoader = loader }
                      .padding(horizontal = 10.dp, vertical = 6.dp)
                  ) {
                    Text(
                      text = loader.displayName,
                      color = if (isLoaderSelected) color else Color.White,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }

            if (modLoader == ModLoader.FABRIC) {
              item {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(IronSurfaceVariant)
                    .clickable { fabricApi = !fabricApi }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                  Checkbox(
                    checked = fabricApi,
                    onCheckedChange = { fabricApi = it },
                    colors = CheckboxDefaults.colors(
                      checkedColor = IronCyan,
                      checkmarkColor = Color(0xFF00363D)
                    )
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Column {
                    Text(
                      text = "Install Fabric API Automatically",
                      color = Color.White,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "Recommended for Fabric mod compatibility",
                      color = Color(0xFF8B949E),
                      fontSize = 11.sp
                    )
                  }
                }
              }
            }

            item {
              Text(
                text = "Allocated RAM: ${ramMb.toInt()} MB (${(ramMb / 1024f).let { "%.1f".format(it) }} GB)",
                color = IronCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Slider(
                value = ramMb,
                onValueChange = { ramMb = it },
                valueRange = 1024f..8192f,
                steps = 6,
                colors = SliderDefaults.colors(
                  thumbColor = IronCyan,
                  activeTrackColor = IronCyan,
                  inactiveTrackColor = IronSurfaceVariant
                )
              )
            }

            item {
              OutlinedTextField(
                value = jvmArgs,
                onValueChange = { jvmArgs = it },
                label = { Text("JVM Arguments") },
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
        },
        confirmButton = {
          Button(
            onClick = {
              viewModel.createOrUpdateProfile(
                id = initial?.id,
                name = name,
                version = version,
                modLoader = modLoader,
                fabricApi = fabricApi,
                ramMb = ramMb.toInt(),
                jvmArgs = jvmArgs
              )
              showCreateDialog = false
              editingProfile = null
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = IronCyan,
              contentColor = Color(0xFF00363D)
            )
          ) {
            Text(if (isEditing) "Save Changes" else "Create Profile", fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(
            onClick = {
              showCreateDialog = false
              editingProfile = null
            },
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF8B949E))
          ) {
            Text("Cancel")
          }
        }
      )
    }
  }
}
