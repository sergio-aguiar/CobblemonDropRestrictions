package net.cobblemondroprestrictions.command

import net.cobblemondroprestrictions.config.ConfigManager
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ReloadCommand {
    fun register(dispatcher: com.mojang.brigadier.CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("cobblemon_drop_restrictions")
                .requires { it.hasPermissionLevel(2) }
                .then(
                    literal("reload")
                        .executes { context ->
                            val config = ConfigManager.reload()
                            context.source.sendFeedback(
                                {
                                    Text.literal(
                                        "Reloaded cobblemon_drop_restrictions config (${config.entities.size} configured entity id(s))"
                                    ).formatted(Formatting.GREEN)
                                },
                                true
                            )
                            1
                        }
                )
        )
    }
}
