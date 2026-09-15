package net.cobblemondroprestrictions.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccess {
    @Accessor("attackingPlayer")
    PlayerEntity cobblemonDropRestrictions_getAttackingPlayer();

    @Accessor("playerHitTimer")
    int cobblemonDropRestrictions_getPlayerHitTimer();
}
