package com.example.data.model

enum class ModLoader(val displayName: String, val badgeColorHex: Long) {
  VANILLA("Vanilla", 0xFF78909C),
  FABRIC("Fabric", 0xFFB388FF),
  FORGE("Forge", 0xFFFF9100),
  NEOFORGE("NeoForge", 0xFFFF6D00),
  QUILT("Quilt", 0xFF80D8FF)
}

enum class ServerType(val displayName: String, val badgeColorHex: Long, val description: String) {
  PAPER("Paper", 0xFF40C4FF, "High performance, async chunk loading & TPS stability"),
  PURPUR("Purpur", 0xFFE040FB, "Ultra customizable Paper fork with enhanced gameplay tweaks"),
  FABRIC("Fabric Server", 0xFFB388FF, "Lightweight server for Fabric mods & Fabric API"),
  FORGE("Forge Server", 0xFFFF9100, "Modded server supporting extensive Forge mods"),
  VANILLA("Vanilla", 0xFF90A4AE, "Standard official Mojang server software"),
  SPIGOT("Spigot", 0xFFFFAB40, "Classic Bukkit/Spigot plugin-supported server")
}

enum class ModCategory(val displayName: String) {
  ALL("All"),
  API("API & Core"),
  OPTIMIZATION("Optimization"),
  TECH("Tech & Machines"),
  MAGIC("Magic"),
  UTILITY("Utility & UI"),
  SHADERS("Shaders & Visuals")
}

data class GameProfile(
  val id: String,
  val name: String,
  val gameVersion: String,
  val modLoader: ModLoader,
  val fabricApiInstalled: Boolean = false,
  val allocatedRamMb: Int = 2048,
  val jvmArgs: String = "-XX:+UseG1GC -Dsun.rmi.dgc.server.gcInterval=2147483646",
  val resolution: String = "1920x1080",
  val lastPlayedEpoch: Long = System.currentTimeMillis()
)

data class ModItem(
  val id: String,
  val name: String,
  val description: String,
  val version: String,
  val loader: ModLoader,
  val category: ModCategory,
  val isEnabled: Boolean = true,
  val fileSize: String = "1.2 MB",
  val isFabricApi: Boolean = false,
  val downloadCount: String = "1.2M",
  val author: String = "Iron Community"
)

data class ServerConfig(
  val id: String = "default_server",
  val name: String = "Iron Local Host",
  val serverType: ServerType = ServerType.PAPER,
  val gameVersion: String = "1.21.4",
  val port: Int = 25565,
  val minRamMb: Int = 1024,
  val maxRamMb: Int = 3072,
  val cpuThreads: Int = 4,
  val useAikarFlags: Boolean = true,
  val eulaAccepted: Boolean = true,
  val onlineMode: Boolean = false,
  val maxPlayers: Int = 20,
  val viewDistance: Int = 10,
  val motd: String = "§b§lIron Launcher Server §7- §aReady to Play!",
  val difficulty: String = "normal",
  val gameMode: String = "survival",
  val pvp: Boolean = true,
  val worldSeed: String = ""
)

enum class ServerStatus {
  OFFLINE,
  STARTING,
  RUNNING,
  STOPPING
}

data class ServerConsoleLog(
  val id: Long,
  val timestamp: String,
  val level: String,
  val message: String
)

data class PlayerAccount(
  val username: String = "IronCrafter",
  val isOnlineAuth: Boolean = false,
  val skinName: String = "Iron Knight",
  val capeName: String = "Founder Cyan"
)
