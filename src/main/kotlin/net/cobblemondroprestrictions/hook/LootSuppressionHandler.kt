package net.cobblemondroprestrictions.hook

import net.cobblemondroprestrictions.debug.DebugLogger
import net.cobblemondroprestrictions.filter.EntityFilter
import net.cobblemondroprestrictions.validation.DeathDecisionCache
import net.cobblemondroprestrictions.validation.KillValidationEngine
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource

object LootSuppressionHandler {
    @JvmStatic
    fun record(entity: LivingEntity, damageSource: DamageSource) {
        if (!EntityFilter.isManaged(entity)) {
            return
        }

        val allowed = KillValidationEngine.shouldAllowLoot(entity, damageSource)
        if (DeathDecisionCache.record(entity.uuid, damageSource, allowed)) {
            DebugLogger.logDecision(entity, damageSource, allowed)
        }
    }

    @JvmStatic
    fun evaluate(entity: LivingEntity, damageSource: DamageSource): Boolean {
        if (!EntityFilter.isManaged(entity)) {
            return true
        }

        record(entity, damageSource)
        return DeathDecisionCache.peek(entity.uuid)?.allowed == true
    }
}
