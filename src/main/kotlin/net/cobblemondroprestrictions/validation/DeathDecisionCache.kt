package net.cobblemondroprestrictions.validation

import net.minecraft.entity.damage.DamageSource
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object DeathDecisionCache {
    data class CachedDecision(
        val source: DamageSource,
        val allowed: Boolean
    )

    private val decisions = ConcurrentHashMap<UUID, CachedDecision>()

    fun record(entityId: UUID, source: DamageSource, allowed: Boolean): Boolean {
        val existing = decisions[entityId]
        if (existing?.allowed == true && !allowed) {
            return false
        }

        decisions[entityId] = CachedDecision(source, allowed)
        return true
    }

    fun peek(entityId: UUID): CachedDecision? = decisions[entityId]

    fun clear(entityId: UUID) {
        decisions.remove(entityId)
    }
}
