package com.example.data.repository

import com.example.data.model.GameProfile
import com.example.data.model.ModCategory
import com.example.data.model.ModItem
import com.example.data.model.ModLoader
import com.example.data.model.PlayerAccount
import com.example.data.model.ServerConfig
import com.example.data.model.ServerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class LauncherRepository {

  val availableVersions = listOf(
    "26.3",
    "26.2",
    "1.21.4",
    "1.21.1",
    "1.20.6",
    "1.20.4",
    "1.20.1",
    "1.19.4",
    "1.18.2",
    "1.16.5",
    "1.12.2",
    "1.8.9"
  )

  private val _profiles = MutableStateFlow<List<GameProfile>>(
    listOf(
      GameProfile(
        id = "profile_fabric_1214",
        name = "Iron Fabric 1.21.4",
        gameVersion = "1.21.4",
        modLoader = ModLoader.FABRIC,
        fabricApiInstalled = true,
        allocatedRamMb = 3072,
        jvmArgs = "-XX:+UseG1GC -XX:+UnlockExperimentalVMOptions"
      ),
      GameProfile(
        id = "profile_future_263",
        name = "NextGen Snapshot 26.3",
        gameVersion = "26.3",
        modLoader = ModLoader.FABRIC,
        fabricApiInstalled = true,
        allocatedRamMb = 4096
      ),
      GameProfile(
        id = "profile_forge_1201",
        name = "Create Tech Pack (Forge)",
        gameVersion = "1.20.1",
        modLoader = ModLoader.FORGE,
        fabricApiInstalled = false,
        allocatedRamMb = 4096
      ),
      GameProfile(
        id = "profile_vanilla_262",
        name = "Pure Vanilla 26.2",
        gameVersion = "26.2",
        modLoader = ModLoader.VANILLA,
        fabricApiInstalled = false,
        allocatedRamMb = 2048
      )
    )
  )
  val profiles: StateFlow<List<GameProfile>> = _profiles.asStateFlow()

  private val _selectedProfileId = MutableStateFlow("profile_fabric_1214")
  val selectedProfileId: StateFlow<String> = _selectedProfileId.asStateFlow()

  private val _mods = MutableStateFlow<List<ModItem>>(
    listOf(
      ModItem(
        id = "mod_fabric_api",
        name = "Fabric API",
        description = "Core essential hook library required by 99% of Fabric mods to function.",
        version = "0.110.0+1.21.4",
        loader = ModLoader.FABRIC,
        category = ModCategory.API,
        isEnabled = true,
        fileSize = "2.4 MB",
        isFabricApi = true,
        downloadCount = "48.2M",
        author = "FabricMC Team"
      ),
      ModItem(
        id = "mod_sodium",
        name = "Sodium",
        description = "Modern rendering optimization engine that dramatically increases frame rates (FPS).",
        version = "mc1.21.4-0.6.2",
        loader = ModLoader.FABRIC,
        category = ModCategory.OPTIMIZATION,
        isEnabled = true,
        fileSize = "1.8 MB",
        downloadCount = "29.1M",
        author = "CaffeineMC"
      ),
      ModItem(
        id = "mod_iris",
        name = "Iris Shaders",
        description = "Modern shaderpack loader compatible with Sodium shaders for breathtaking lighting.",
        version = "1.8.0-1.21.4",
        loader = ModLoader.FABRIC,
        category = ModCategory.SHADERS,
        isEnabled = true,
        fileSize = "3.2 MB",
        downloadCount = "18.4M",
        author = "Iris Team"
      ),
      ModItem(
        id = "mod_lithium",
        name = "Lithium",
        description = "General-purpose physics, mob AI, and chunk tick optimization mod.",
        version = "mc1.21.4-0.14.0",
        loader = ModLoader.FABRIC,
        category = ModCategory.OPTIMIZATION,
        isEnabled = true,
        fileSize = "920 KB",
        downloadCount = "14.5M",
        author = "CaffeineMC"
      ),
      ModItem(
        id = "mod_jei",
        name = "Just Enough Items (JEI)",
        description = "Item and recipe viewer with fast search, crafting recipes, and potion lookups.",
        version = "19.21.0.24",
        loader = ModLoader.FORGE,
        category = ModCategory.UTILITY,
        isEnabled = true,
        fileSize = "1.6 MB",
        downloadCount = "120M",
        author = "mezz"
      ),
      ModItem(
        id = "mod_journeymap",
        name = "JourneyMap",
        description = "Real-time mapping in-game or in a web browser as you explore your Minecraft world.",
        version = "1.21.4-6.0.0",
        loader = ModLoader.FABRIC,
        category = ModCategory.UTILITY,
        isEnabled = true,
        fileSize = "4.5 MB",
        downloadCount = "65.7M",
        author = "Techbrew"
      ),
      ModItem(
        id = "mod_create",
        name = "Create Mod",
        description = "Mechanical tools, automation, rotating kinetic energy, trains, and factory contraptions.",
        version = "0.5.1j",
        loader = ModLoader.FORGE,
        category = ModCategory.TECH,
        isEnabled = true,
        fileSize = "18.4 MB",
        downloadCount = "32.1M",
        author = "simibubi"
      ),
      ModItem(
        id = "mod_appleskin",
        name = "AppleSkin",
        description = "Adds hunger and saturation HUD indicators and food value tooltips.",
        version = "3.0.5",
        loader = ModLoader.FABRIC,
        category = ModCategory.UTILITY,
        isEnabled = true,
        fileSize = "340 KB",
        downloadCount = "40.9M",
        author = "squeek502"
      ),
      ModItem(
        id = "mod_biomes_o_plenty",
        name = "Biomes O' Plenty",
        description = "Adds over 80 new unique biomes, building blocks, foliage, and gems to explore.",
        version = "19.0.0.95",
        loader = ModLoader.FORGE,
        category = ModCategory.MAGIC,
        isEnabled = false,
        fileSize = "12.8 MB",
        downloadCount = "45.0M",
        author = "Glitchfiend"
      )
    )
  )
  val mods: StateFlow<List<ModItem>> = _mods.asStateFlow()

  private val _serverConfig = MutableStateFlow(
    ServerConfig(
      id = "server_paper_default",
      name = "Iron LAN Dedicated Server",
      serverType = ServerType.PAPER,
      gameVersion = "1.21.4",
      port = 25565,
      minRamMb = 1024,
      maxRamMb = 3072,
      cpuThreads = 4,
      useAikarFlags = true,
      eulaAccepted = true,
      onlineMode = false,
      maxPlayers = 20,
      motd = "§b§lIron Launcher Server §7- §aRunning on Android Device!"
    )
  )
  val serverConfig: StateFlow<ServerConfig> = _serverConfig.asStateFlow()

  private val _playerAccount = MutableStateFlow(
    PlayerAccount(
      username = "IronCrafter",
      isOnlineAuth = false,
      skinName = "Iron Knight",
      capeName = "Founder Cyan"
    )
  )
  val playerAccount: StateFlow<PlayerAccount> = _playerAccount.asStateFlow()

  fun selectProfile(id: String) {
    _selectedProfileId.value = id
  }

  fun saveProfile(profile: GameProfile) {
    val existing = _profiles.value.toMutableList()
    val index = existing.indexOfFirst { it.id == profile.id }
    if (index >= 0) {
      existing[index] = profile
    } else {
      existing.add(profile)
    }
    _profiles.value = existing
    _selectedProfileId.value = profile.id
  }

  fun deleteProfile(id: String) {
    if (_profiles.value.size <= 1) return
    _profiles.value = _profiles.value.filter { it.id != id }
    if (_selectedProfileId.value == id) {
      _selectedProfileId.value = _profiles.value.first().id
    }
  }

  fun toggleMod(modId: String) {
    _mods.value = _mods.value.map {
      if (it.id == modId) it.copy(isEnabled = !it.isEnabled) else it
    }
  }

  fun installFabricApi(gameVersion: String) {
    val existing = _mods.value.find { it.isFabricApi }
    if (existing != null) {
      _mods.value = _mods.value.map {
        if (it.id == existing.id) it.copy(isEnabled = true, version = "0.110.0+$gameVersion") else it
      }
    } else {
      val fabricApi = ModItem(
        id = "mod_fabric_api_${System.currentTimeMillis()}",
        name = "Fabric API",
        description = "Core essential hook library required by 99% of Fabric mods to function.",
        version = "0.110.0+$gameVersion",
        loader = ModLoader.FABRIC,
        category = ModCategory.API,
        isEnabled = true,
        fileSize = "2.4 MB",
        isFabricApi = true,
        downloadCount = "50M+",
        author = "FabricMC Team"
      )
      _mods.value = listOf(fabricApi) + _mods.value
    }

    // Also update selected profile if it is Fabric
    val curProfile = _profiles.value.find { it.id == _selectedProfileId.value }
    if (curProfile != null && curProfile.modLoader == ModLoader.FABRIC) {
      saveProfile(curProfile.copy(fabricApiInstalled = true))
    }
  }

  fun addCustomMod(name: String, version: String, loader: ModLoader, category: ModCategory) {
    val newMod = ModItem(
      id = "custom_mod_${UUID.randomUUID()}",
      name = name,
      description = "User imported custom mod jar for $loader",
      version = version,
      loader = loader,
      category = category,
      isEnabled = true,
      fileSize = "1.5 MB",
      author = "Local User"
    )
    _mods.value = _mods.value + newMod
  }

  fun updateServerConfig(config: ServerConfig) {
    _serverConfig.value = config
  }

  fun updatePlayerAccount(account: PlayerAccount) {
    _playerAccount.value = account
  }
}
