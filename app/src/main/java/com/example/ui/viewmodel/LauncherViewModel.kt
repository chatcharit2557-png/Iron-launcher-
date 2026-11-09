package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GameProfile
import com.example.data.model.ModCategory
import com.example.data.model.ModItem
import com.example.data.model.ModLoader
import com.example.data.model.PlayerAccount
import com.example.data.model.ServerConfig
import com.example.data.model.ServerStatus
import com.example.data.model.ServerType
import com.example.data.repository.LauncherRepository
import com.example.data.service.DeviceHardwareInfo
import com.example.data.service.LocalServerEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

sealed interface NavigationDestination {
  object Home : NavigationDestination
  object Profiles : NavigationDestination
  object Mods : NavigationDestination
  object Server : NavigationDestination
  object Account : NavigationDestination
}

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = LauncherRepository()
  val serverEngine = LocalServerEngine(application.applicationContext)

  val profiles = repository.profiles
  val selectedProfileId = repository.selectedProfileId
  val mods = repository.mods
  val serverConfig = repository.serverConfig
  val playerAccount = repository.playerAccount
  val availableVersions = repository.availableVersions

  val serverStatus = serverEngine.status
  val serverLogs = serverEngine.logs
  val serverCpuUsage = serverEngine.cpuUsagePercent
  val serverRamUsageMb = serverEngine.serverRamUsedMb
  val serverTps = serverEngine.tps

  private val _deviceHardware = MutableStateFlow(serverEngine.getDeviceHardwareInfo())
  val deviceHardware: StateFlow<DeviceHardwareInfo> = _deviceHardware.asStateFlow()

  private val _localIp = MutableStateFlow(serverEngine.getLocalIpAddress())
  val localIp: StateFlow<String> = _localIp.asStateFlow()

  // Active Screen Navigation
  private val _currentDestination = MutableStateFlow<NavigationDestination>(NavigationDestination.Home)
  val currentDestination: StateFlow<NavigationDestination> = _currentDestination.asStateFlow()

  // Selected Profile derived
  val activeProfile: StateFlow<GameProfile?> = combine(profiles, selectedProfileId) { profList, selId ->
    profList.find { it.id == selId } ?: profList.firstOrNull()
  }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

  // Launching Game Session State
  private val _isLaunching = MutableStateFlow(false)
  val isLaunching: StateFlow<Boolean> = _isLaunching.asStateFlow()

  private val _launchProgress = MutableStateFlow(0f)
  val launchProgress: StateFlow<Float> = _launchProgress.asStateFlow()

  private val _launchStatusText = MutableStateFlow("")
  val launchStatusText: StateFlow<String> = _launchStatusText.asStateFlow()

  private val _isGameSessionActive = MutableStateFlow(false)
  val isGameSessionActive: StateFlow<Boolean> = _isGameSessionActive.asStateFlow()

  // Mod Search & Filter
  private val _modSearchQuery = MutableStateFlow("")
  val modSearchQuery: StateFlow<String> = _modSearchQuery.asStateFlow()

  private val _selectedModCategory = MutableStateFlow(ModCategory.ALL)
  val selectedModCategory: StateFlow<ModCategory> = _selectedModCategory.asStateFlow()

  val filteredMods: StateFlow<List<ModItem>> = combine(mods, _modSearchQuery, _selectedModCategory) { modList, query, cat ->
    modList.filter { mod ->
      val matchesQuery = query.isBlank() || mod.name.contains(query, ignoreCase = true) || mod.description.contains(query, ignoreCase = true)
      val matchesCat = (cat == ModCategory.ALL) || (mod.category == cat)
      matchesQuery && matchesCat
    }
  }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  private var launchJob: Job? = null

  fun navigateTo(dest: NavigationDestination) {
    _currentDestination.value = dest
  }

  fun refreshHardwareInfo() {
    _deviceHardware.value = serverEngine.getDeviceHardwareInfo()
    _localIp.value = serverEngine.getLocalIpAddress()
  }

  fun selectProfile(id: String) {
    repository.selectProfile(id)
  }

  fun createOrUpdateProfile(
    id: String? = null,
    name: String,
    version: String,
    modLoader: ModLoader,
    fabricApi: Boolean,
    ramMb: Int,
    jvmArgs: String
  ) {
    val profileId = id ?: "profile_${UUID.randomUUID().toString().take(8)}"
    val newProfile = GameProfile(
      id = profileId,
      name = name.ifBlank { "Minecraft $version ($modLoader)" },
      gameVersion = version,
      modLoader = modLoader,
      fabricApiInstalled = fabricApi,
      allocatedRamMb = ramMb,
      jvmArgs = jvmArgs
    )
    repository.saveProfile(newProfile)
  }

  fun deleteProfile(id: String) {
    repository.deleteProfile(id)
  }

  fun toggleMod(modId: String) {
    repository.toggleMod(modId)
  }

  fun installFabricApiForCurrent() {
    val version = activeProfile.value?.gameVersion ?: "1.21.4"
    repository.installFabricApi(version)
  }

  fun addCustomMod(name: String, version: String, loader: ModLoader, category: ModCategory) {
    repository.addCustomMod(name, version, loader, category)
  }

  fun setModSearch(q: String) {
    _modSearchQuery.value = q
  }

  fun setModCategory(cat: ModCategory) {
    _selectedModCategory.value = cat
  }

  // Launching game
  fun launchGame() {
    if (_isLaunching.value || _isGameSessionActive.value) return
    val profile = activeProfile.value ?: return

    _isLaunching.value = true
    _launchProgress.value = 0f
    _launchStatusText.value = "Preparing launch for ${profile.name}..."

    launchJob?.cancel()
    launchJob = viewModelScope.launch {
      delay(300)
      _launchStatusText.value = "Verifying version ${profile.gameVersion} client binaries..."
      _launchProgress.value = 0.2f

      delay(500)
      if (profile.modLoader == ModLoader.FABRIC) {
        _launchStatusText.value = "Injecting Fabric Loader 0.16.9 and Fabric API..."
      } else if (profile.modLoader == ModLoader.FORGE) {
        _launchStatusText.value = "Patching Forge ModLoader 53.0.x classpaths..."
      } else {
        _launchStatusText.value = "Loading Vanilla Mojang assets..."
      }
      _launchProgress.value = 0.45f

      delay(500)
      _launchStatusText.value = "Allocating ${profile.allocatedRamMb} MB JVM Heap & OpenGL context..."
      _launchProgress.value = 0.75f

      delay(400)
      _launchStatusText.value = "Launching Minecraft Java Edition window..."
      _launchProgress.value = 1.0f

      delay(300)
      _isLaunching.value = false
      _isGameSessionActive.value = true
    }
  }

  fun exitGameSession() {
    _isGameSessionActive.value = false
    _isLaunching.value = false
  }

  // Server management
  fun startServer() {
    serverEngine.startServer(serverConfig.value)
  }

  fun stopServer() {
    serverEngine.stopServer()
  }

  fun restartServer() {
    serverEngine.restartServer(serverConfig.value)
  }

  fun executeServerCommand(cmd: String) {
    serverEngine.executeCommand(cmd)
  }

  fun clearServerLogs() {
    serverEngine.clearLogs()
  }

  fun updateServerSettings(
    serverType: ServerType,
    version: String,
    port: Int,
    minRam: Int,
    maxRam: Int,
    cpuThreads: Int,
    onlineMode: Boolean,
    maxPlayers: Int,
    motd: String,
    viewDist: Int
  ) {
    val updated = serverConfig.value.copy(
      serverType = serverType,
      gameVersion = version,
      port = port,
      minRamMb = minRam,
      maxRamMb = maxRam,
      cpuThreads = cpuThreads,
      onlineMode = onlineMode,
      maxPlayers = maxPlayers,
      motd = motd,
      viewDistance = viewDist
    )
    repository.updateServerConfig(updated)
  }

  fun updateAccount(username: String, skin: String, cape: String) {
    repository.updatePlayerAccount(
      playerAccount.value.copy(
        username = username.ifBlank { "IronCrafter" },
        skinName = skin,
        capeName = cape
      )
    )
  }
}
