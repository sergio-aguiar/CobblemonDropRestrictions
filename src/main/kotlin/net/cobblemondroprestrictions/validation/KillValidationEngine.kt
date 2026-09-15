package net.cobblemondroprestrictions.validation

import net.cobblemondroprestrictions.config.ConfigManager
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.TntEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.projectile.ProjectileEntity

object KillValidationEngine {
    fun shouldAllowLoot(entity: LivingEntity, source: DamageSource): Boolean {
        if (ConfigManager.allowTamedEntities()) {
            return DamageAttribution.resolvePlayer(source) != null
        }

        if (isTamedEntityKill(source)) {
            return false
        }

        return DamageAttribution.resolvePlayer(source) != null
    }

    fun describeAttribution(source: DamageSource): String = DamageAttribution.describe(source)

    private fun isTamedEntityKill(source: DamageSource): Boolean {
        val directAttacker = source.attacker
        if (directAttacker is TameableEntity && directAttacker.isTamed) {
            return true
        }

        val directSource = source.source
        if (directSource is TameableEntity && directSource.isTamed) {
            return true
        }

        if (directAttacker is ProjectileEntity) {
            val owner = directAttacker.owner
            if (owner is TameableEntity && owner.isTamed) {
                return true
            }
        }

        if (directAttacker is TntEntity) {
            val owner = directAttacker.owner
            if (owner is TameableEntity && owner.isTamed) {
                return true
            }
        }

        return false
    }
}
