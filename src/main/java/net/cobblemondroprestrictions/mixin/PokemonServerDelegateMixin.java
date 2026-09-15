package net.cobblemondroprestrictions.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonServerDelegate;
import net.cobblemondroprestrictions.cobblemon.CobblemonDropGuard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PokemonServerDelegate.class, remap = false)
public abstract class PokemonServerDelegateMixin {
    @Shadow(remap = false)
    public PokemonEntity entity;

    @Inject(method = "doDeathDrops", at = @At("HEAD"), cancellable = true, remap = false)
    private void cobblemonDropRestrictions$guardDeathDrops(CallbackInfo ci) {
        if (!CobblemonDropGuard.shouldAllow(entity)) {
            CobblemonDropGuard.clearTracking(entity);
            ci.cancel();
        }
    }
}
