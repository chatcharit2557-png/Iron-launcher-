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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModCategory
import com.example.data.model.ModLoader
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.FabricPurple
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.IronCyan
import com.example.ui.theme.IronDarkBg
import com.example.ui.theme.IronSurface
import com.example.ui.theme.IronSurfaceBorder
import com.example.ui.theme.IronSurfaceVariant
import com.example.ui.viewmodel.LauncherViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModsScreen(viewModel: LauncherViewModel) {
  val filteredMods by viewModel.filteredMods.collectAsState()
  val activeProfile by viewModel.activeProfile.collectAsState()
  val searchQuery by viewModel.modSearchQuery.collectAsState()
  val selectedCategory by viewModel.selectedModCategory.collectAsState()

  var showAddModDialog by remember { mutableStateOf(false) }

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
              text = "MOD & ADDON MANAGER",
              color = IronCyan,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Text(
              text = "Fabric & Forge Mods",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
          val activeCount = filteredMods.count { it.isEnabled }
          Text(
            text = "$activeCount / ${filteredMods.size} Enabled",
            color = Color(0xFF8B949E),
            fontSize = 12.sp
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
      }

      // Special 1-Click Fabric API Banner
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, FabricPurple, RoundedCornerShape(14.dp)),
          colors = CardDefaults.cardColors(containerColor = IronSurface),
          shape = RoundedCornerShape(14.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(FabricPurple.copy(alpha = 0.2f))
                  .border(1.dp, FabricPurple, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Extension,
                  contentDescription = null,
                  tint = FabricPurple,
                  modifier = Modifier.size(22.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Fabric API 1-Click Install",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
                Text(
                  text = "Essential core hooks required by all Fabric mods",
                  color = Color(0xFFBAC7D5),
                  fontSize = 12.sp
                )
              }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
              onClick = { viewModel.installFabricApiForCurrent() },
              colors = ButtonDefaults.buttonColors(
                containerColor = FabricPurple,
                contentColor = Color(0xFF1E0C38)
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("install_fabric_api_button")
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Download,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Install API", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // Search Bar
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { viewModel.setModSearch(it) },
          placeholder = { Text("Search mods (e.g. Sodium, JEI, Iris, Create)...") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = Color(0xFF8B949E)
            )
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IronCyan,
            unfocusedBorderColor = IronSurfaceBorder,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = IronSurface,
            unfocusedContainerColor = IronSurface
          ),
          singleLine = true
        )
      }

      // Category Chips
      item {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(ModCategory.values()) { cat ->
            val isSelected = selectedCategory == cat
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(if (isSelected) Color(0xFF00363D) else IronSurfaceVariant)
                .border(
                  1.dp,
                  if (isSelected) IronCyan else IronSurfaceBorder,
                  RoundedCornerShape(20.dp)
                )
                .clickable { viewModel.setModCategory(cat) }
                .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
              Text(
                text = cat.displayName,
                color = if (isSelected) IronCyan else Color.White,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }
      }

      // Mod Items List
      items(filteredMods, key = { it.id }) { mod ->
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
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                val loaderColor = Color(mod.loader.badgeColorHex)
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(loaderColor.copy(alpha = 0.2f))
                    .border(1.dp, loaderColor, RoundedCornerShape(8.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Extension,
                    contentDescription = null,
                    tint = loaderColor,
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = mod.name,
                      color = Color.White,
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold
                    )
                    if (mod.isFabricApi) {
                      Spacer(modifier = Modifier.width(6.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(Color(0xFF00363D))
                          .padding(horizontal = 5.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = "CORE API",
                          color = IronCyan,
                          fontSize = 9.sp,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                  }
                  Text(
                    text = "v${mod.version} • by ${mod.author}",
                    color = Color(0xFF8B949E),
                    fontSize = 11.sp
                  )
                }
              }

              // Toggle Switch
              Switch(
                checked = mod.isEnabled,
                onCheckedChange = { viewModel.toggleMod(mod.id) },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = IronCyan,
                  checkedTrackColor = Color(0xFF00363D),
                  uncheckedThumbColor = Color(0xFF8B949E),
                  uncheckedTrackColor = IronSurfaceVariant
                ),
                modifier = Modifier.testTag("toggle_mod_${mod.id}")
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = mod.description,
              color = Color(0xFFBAC7D5),
              fontSize = 12.sp,
              lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Loader badge
                val loaderColor = Color(mod.loader.badgeColorHex)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(loaderColor.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = mod.loader.displayName,
                    color = loaderColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }

                // Category badge
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(IronSurfaceVariant)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = mod.category.displayName,
                    color = Color(0xFF8B949E),
                    fontSize = 10.sp
                  )
                }
              }

              Text(
                text = "${mod.fileSize} • ${mod.downloadCount} downloads",
                color = Color(0xFF8B949E),
                fontSize = 11.sp
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }

    // Add Custom Mod FAB
    FloatingActionButton(
      onClick = { showAddModDialog = true },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("add_custom_mod_fab"),
      containerColor = IronCyan,
      contentColor = Color(0xFF00363D),
      shape = CircleShape
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Add Mod",
        modifier = Modifier.size(24.dp)
      )
    }

    // Add Custom Mod Dialog
    if (showAddModDialog) {
      var customName by remember { mutableStateOf("") }
      var customVersion by remember { mutableStateOf("1.0.0") }
      var customLoader by remember { mutableStateOf(ModLoader.FABRIC) }
      var customCategory by remember { mutableStateOf(ModCategory.UTILITY) }

      AlertDialog(
        onDismissRequest = { showAddModDialog = false },
        containerColor = IronSurface,
        title = {
          Text(
            text = "Import Custom Mod (.jar)",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
              value = customName,
              onValueChange = { customName = it },
              label = { Text("Mod Name") },
              placeholder = { Text("e.g. WorldEdit") },
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IronCyan,
                unfocusedBorderColor = IronSurfaceBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
              ),
              singleLine = true
            )

            OutlinedTextField(
              value = customVersion,
              onValueChange = { customVersion = it },
              label = { Text("Version") },
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
              text = "Target Loader",
              color = IronCyan,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              listOf(ModLoader.FABRIC, ModLoader.FORGE, ModLoader.NEOFORGE).forEach { loader ->
                val isSelected = customLoader == loader
                val color = Color(loader.badgeColorHex)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) color.copy(alpha = 0.25f) else IronSurfaceVariant)
                    .border(
                      1.dp,
                      if (isSelected) color else IronSurfaceBorder,
                      RoundedCornerShape(6.dp)
                    )
                    .clickable { customLoader = loader }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = loader.displayName,
                    color = if (isSelected) color else Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        },
        confirmButton = {
          Button(
            onClick = {
              if (customName.isNotBlank()) {
                viewModel.addCustomMod(
                  name = customName,
                  version = customVersion,
                  loader = customLoader,
                  category = customCategory
                )
              }
              showAddModDialog = false
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = IronCyan,
              contentColor = Color(0xFF00363D)
            )
          ) {
            Text("Add to Profile", fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(
            onClick = { showAddModDialog = false },
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF8B949E))
          ) {
            Text("Cancel")
          }
        }
      )
    }
  }
}
