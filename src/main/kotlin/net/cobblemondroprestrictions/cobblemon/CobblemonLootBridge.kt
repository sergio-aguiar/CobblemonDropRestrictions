package net.cobblemondroprestrictions.cobblemon

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.cobblemondroprestrictions.debug.DebugLogger
import net.cobblemondroprestrictions.filter.EntityFilter
import net.cobblemondroprestrictions.validation.DeathDecisionCache
import net.minecraft.entity.LivingEntity

object CobblemonLootBridge {
    fun register() {
        CobblemonEvents.LOOT_DROPPED.subscribe { event ->
            val entity = event.entity as? LivingEntity ?: return@subscribe
            if (!EntityFilter.isManaged(entity)) {
                return@subscribe
            }

            val allowed = CobblemonDropGuard.shouldAllow(entity)

            val cached = DeathDecisionCache.peek(entity.uuid)
            if (cached != null) {
                DebugLogger.logDecision(entity, cached.source, allowed)
            } else {
                DebugLogger.logPlayerDecision(entity, event.player, allowed)
            }

            if (!allowed) {
                event.cancel()
            }

            CobblemonDropGuard.clearTracking(entity)
        }
    }
}
