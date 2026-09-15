package net.cobblemondroprestrictions

import net.cobblemondroprestrictions.cobblemon.CobblemonLootBridge
import net.cobblemondroprestrictions.command.ReloadCommand
import net.cobblemondroprestrictions.config.ConfigManager
import net.cobblemondroprestrictions.hook.LootSuppressionHandler
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.LoggerFactory

object CobblemonDropRestrictions : ModInitializer {
    const val MOD_ID = "cobblemon_drop_restrictions"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        LOGGER.info("Initializing Cobblemon Drop Restrictions")

        ConfigManager.initialize()
        CobblemonLootBridge.register()

        ServerLifecycleEvents.SERVER_STARTED.register {
            ConfigManager.reload()
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            ReloadCommand.register(dispatcher)
        }

        LOGGER.info("Cobblemon Drop Restrictions initialized")
    }
}
