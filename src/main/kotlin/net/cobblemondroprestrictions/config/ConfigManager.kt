package net.cobblemondroprestrictions.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object ConfigManager {
    private val logger = LoggerFactory.getLogger("cobblemon_drop_restrictions")
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    private val configDir: Path = FabricLoader.getInstance().configDir.resolve("cobblemon_drop_restrictions")
    private val configFile: Path = configDir.resolve("config.json")

    private var config: ModConfig = ModConfig()
    private var managedEntities: Set<Identifier> = emptySet()

    fun initialize() {
        reload()
    }

    fun reload(): ModConfig {
        if (!Files.exists(configFile)) {
            writeDefaultConfig()
        }

        config = try {
            Files.newBufferedReader(configFile).use { reader ->
                gson.fromJson(reader, ModConfig::class.java)
            } ?: ModConfig()
        } catch (e: JsonSyntaxException) {
            logger.error("Failed to parse config at $configFile, using defaults", e)
            ModConfig()
        } catch (e: IOException) {
            logger.error("Failed to read config at $configFile, using defaults", e)
            ModConfig()
        }

        managedEntities = buildManagedEntitySet(config.entities)
        logger.info("Loaded ${managedEntities.size} managed entity type(s)")
        return config
    }

    fun getConfig(): ModConfig = config

    fun isDebugEnabled(): Boolean = config.debug

    fun isManagedEntity(entityId: Identifier): Boolean = managedEntities.contains(entityId)

    fun allowTamedEntities(): Boolean = config.allowTamedEntities

    private fun buildManagedEntitySet(rawEntities: List<String>): Set<Identifier> {
        val resolved = linkedSetOf<Identifier>()

        for (raw in rawEntities) {
            val id = Identifier.tryParse(raw)
            if (id == null) {
                logger.warn("Skipping invalid entity id in config: $raw")
                continue
            }

            if (!Registries.ENTITY_TYPE.containsId(id)) {
                logger.warn("Entity type not registered, skipping: $id")
                continue
            }

            resolved.add(id)
        }

        return resolved
    }

    private fun writeDefaultConfig() {
        try {
            Files.createDirectories(configDir)
            Files.newBufferedWriter(configFile).use { writer ->
                gson.toJson(ModConfig(), writer)
            }
            logger.info("Created default config at $configFile")
        } catch (e: IOException) {
            logger.error("Failed to write default config at $configFile", e)
        }
    }
}
