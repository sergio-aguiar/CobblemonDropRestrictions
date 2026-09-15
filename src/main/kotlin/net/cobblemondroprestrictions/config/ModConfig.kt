package net.cobblemondroprestrictions.config

data class ModConfig(
    val debug: Boolean = false,
    val entities: List<String> = listOf("minecraft:warden", "cobblemon:pokemon"),
    val allowTamedEntities: Boolean = false
)
