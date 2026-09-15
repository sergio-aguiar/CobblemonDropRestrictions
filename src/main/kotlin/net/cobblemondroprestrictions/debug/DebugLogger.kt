package net.cobblemondroprestrictions.debug

import net.cobblemondroprestrictions.config.ConfigManager
import net.cobblemondroprestrictions.validation.KillValidationEngine
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.registry.Registries
import net.minecraft.server.network.ServerPlayerEntity
import org.slf4j.LoggerFactory

object DebugLogger {
    private val logger = LoggerFactory.getLogger("cobblemon_drop_restrictions")

    fun logDecision(entity: LivingEntity, source: DamageSource, allowed: Boolean) {
        if (!ConfigManager.isDebugEnabled()) {
            return
        }

        val entityId = Registries.ENTITY_TYPE.getId(entity.type)
        val damageType = source.name
        val attacker = KillValidationEngine.describeAttribution(source)

        logger.info(
            """
            |[cobblemon_drop_restrictions]
            |Entity: $entityId
            |Damage: $damageType
            |Attacker: $attacker
            |Decision: ${if (allowed) "ALLOW" else "DENY"}
            """.trimMargin()
        )
    }

    fun logPlayerDecision(entity: LivingEntity, player: ServerPlayerEntity?, allowed: Boolean) {
        if (!ConfigManager.isDebugEnabled()) {
            return
        }

        val entityId = Registries.ENTITY_TYPE.getId(entity.type)
        val attacker = player?.name?.string ?: "none"

        logger.info(
            """
            |[cobblemon_drop_restrictions]
            |Entity: $entityId
            |Source: cobblemon_loot_dropped
            |Attacker: $attacker
            |Decision: ${if (allowed) "ALLOW" else "DENY"}
            """.trimMargin()
        )
    }
}
