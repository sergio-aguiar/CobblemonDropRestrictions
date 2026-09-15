package net.cobblemondroprestrictions.mixin;

import net.cobblemondroprestrictions.hook.LootSuppressionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDeathMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void cobblemonDropRestrictions$recordDeathDecision(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        LootSuppressionHandler.record(self, source);
    }
}
