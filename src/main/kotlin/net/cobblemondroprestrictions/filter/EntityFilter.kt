package net.cobblemondroprestrictions.filter

import net.cobblemondroprestrictions.config.ConfigManager
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Registries

object EntityFilter {
    fun isManaged(entity: LivingEntity): Boolean {
        val id = Registries.ENTITY_TYPE.getId(entity.type)
        return ConfigManager.isManagedEntity(id)
    }
}
