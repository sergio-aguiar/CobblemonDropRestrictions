package net.cobblemondroprestrictions.cobblemon

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.cobblemondroprestrictions.filter.EntityFilter
import net.cobblemondroprestrictions.mixin.LivingEntityAccess
import net.cobblemondroprestrictions.validation.DeathDecisionCache
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity

object CobblemonDropGuard {
    @JvmStatic
    fun shouldAllow(entity: LivingEntity): Boolean {
        if (!EntityFilter.isManaged(entity)) {
            return true
        }

        if (isPlayerBattleFaint(entity)) {
            return true
        }

        DeathDecisionCache.peek(entity.uuid)?.let { return it.allowed }

        return hasRecentPlayerKillCredit(entity)
    }

    @JvmStatic
    fun clearTracking(entity: LivingEntity) {
        DeathDecisionCache.clear(entity.uuid)
    }

    private fun isPlayerBattleFaint(entity: LivingEntity): Boolean {
        val pokemon = entity as? PokemonEntity ?: return false
        if (!pokemon.pokemon.isWild()) {
            return false
        }

        val battle = pokemon.battle ?: return false
        if (battle.ended) {
            return false
        }

        return battle.isPvW
    }

    private fun hasRecentPlayerKillCredit(entity: LivingEntity): Boolean {
        val accessor = entity as? LivingEntityAccess ?: return false
        if (accessor.cobblemonDropRestrictions_getPlayerHitTimer() <= 0) {
            return false
        }

        return accessor.cobblemonDropRestrictions_getAttackingPlayer() is ServerPlayerEntity
    }
}
