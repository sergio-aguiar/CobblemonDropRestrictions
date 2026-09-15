package net.cobblemondroprestrictions.validation

import net.cobblemondroprestrictions.config.ConfigManager
import net.minecraft.entity.Entity
import net.minecraft.entity.TntEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.server.network.ServerPlayerEntity

object DamageAttribution {
    fun resolvePlayer(source: DamageSource): ServerPlayerEntity? {
        resolveFromEntity(source.attacker)?.let { return it }
        resolveFromEntity(source.source)?.let { return it }
        return null
    }

    fun describe(source: DamageSource): String {
        val player = resolvePlayer(source)
        if (player != null) {
            return player.name.string
        }

        val attacker = source.attacker
        if (attacker != null) {
            return attacker.type.translationKey
        }

        val direct = source.source
        if (direct != null) {
            return direct.type.translationKey
        }

        return "unknown"
    }

    private fun resolveFromEntity(entity: Entity?): ServerPlayerEntity? {
        var current: Entity? = entity

        while (current != null) {
            if (current is ServerPlayerEntity) {
                return current
            }

            if (!ConfigManager.allowTamedEntities() && current is TameableEntity) {
                return null
            }

            current = nextOwnerEntity(current)
        }

        return null
    }

    private fun nextOwnerEntity(entity: Entity): Entity? {
        return when (entity) {
            is ProjectileEntity -> entity.owner
            is TntEntity -> entity.owner
            else -> null
        }
    }
}
