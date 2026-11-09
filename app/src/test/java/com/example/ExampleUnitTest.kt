package com.example

import com.example.data.model.ModCategory
import com.example.data.model.ModLoader
import com.example.data.model.ServerType
import com.example.data.repository.LauncherRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testVersionsAndFabricApi() {
    val repo = LauncherRepository()
    // Verify user requested versions e.g. 26.3, 26.2, 1.21.4
    assertTrue(repo.availableVersions.contains("26.3"))
    assertTrue(repo.availableVersions.contains("26.2"))
    assertTrue(repo.availableVersions.contains("1.21.4"))

    val fabricProfile = repo.profiles.value.find { it.modLoader == ModLoader.FABRIC }
    assertNotNull("Fabric profile must be available", fabricProfile)

    val fabricApi = repo.mods.value.find { it.isFabricApi }
    assertNotNull("Fabric API mod must be available", fabricApi)
    assertTrue("Fabric API must be enabled", fabricApi?.isEnabled == true)
  }

  @Test
  fun testServerConfigDefaults() {
    val repo = LauncherRepository()
    val server = repo.serverConfig.value
    assertEquals(ServerType.PAPER, server.serverType)
    assertEquals(25565, server.port)
    assertTrue(server.maxRamMb >= 1024)
    assertTrue(server.cpuThreads >= 1)
  }

  @Test
  fun testProfileSelectionAndModToggle() {
    val repo = LauncherRepository()
    repo.selectProfile("profile_future_263")
    assertEquals("profile_future_263", repo.selectedProfileId.value)

    val firstMod = repo.mods.value.first()
    val initialState = firstMod.isEnabled
    repo.toggleMod(firstMod.id)
    val toggledState = repo.mods.value.first { it.id == firstMod.id }.isEnabled
    assertEquals(!initialState, toggledState)
  }
}
