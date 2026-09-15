package net.cobblemondroprestrictions.mixin;

import net.cobblemondroprestrictions.hook.LootSuppressionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    private void cobblemonDropRestrictions$guardDropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!LootSuppressionHandler.evaluate(self, source)) {
            ci.cancel();
        }
    }
}
