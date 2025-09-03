package de.kaama.modcapture.enchantment;

import dev.architectury.event.EventResult;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

/**
 * This enchantment provides a chance for a player to receive a mob's spawn egg
 * upon killing it. The chance increases with the enchantment's level. Boss mobs
 * are excluded from this effect.
 * <p>
 * This class is a final utility class and cannot be instantiated.
 */
public final class CapturingEnchantment {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("minecraft", "capturing");

    private static final double BASE_CHANCE = 0.05;
    private static final double CHANCE_PER_LEVEL = 0.05;

    private CapturingEnchantment() {
    }

    /**
     * An event listener that triggers when any living entity dies.
     * <p>
     * This method checks if the killer was a player with the Capturing enchantment.
     * If so, it calculates a chance based on the enchantment's level to drop
     * the victim's spawn egg. Boss mobs are always excluded.
     *
     * @param victim The entity that died.
     * @param source The damage source that caused the death.
     * @return Always returns {@link EventResult#pass()} to not interfere with the
     *         death event.
     */
    public static EventResult onEntityDeath(LivingEntity victim, DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) {
            return EventResult.pass();
        }

        EntityType<?> victimType = victim.getType();
        if (victimType == EntityType.ENDER_DRAGON || victimType == EntityType.WITHER) {
            return EventResult.pass();
        }

        Registry<Enchantment> registry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int level = registry.get(ID)
                .map(h -> EnchantmentHelper.getEnchantmentLevel(h, player))
                .orElse(0);

        if (level <= 0) {
            return EventResult.pass();
        }

        double chance = BASE_CHANCE + (level - 1) * CHANCE_PER_LEVEL;
        
        if (player.getRandom().nextDouble() < chance) {
            trySpawnEgg(victim);
        }

        return EventResult.pass();
    }

    /**
     * Attempts to create a spawn egg at the victim's location.
     *
     * @param victim The entity whose spawn egg should be dropped.
     */
    private static void trySpawnEgg(LivingEntity victim) {
        Level world = victim.level();

        if (world.isClientSide)
            return;

        Optional.ofNullable(SpawnEggItem.byId(victim.getType()))
                .ifPresent(egg -> {
                    ItemEntity itemEntity = new ItemEntity(
                            world,
                            victim.getX(),
                            victim.getY(),
                            victim.getZ(),
                            new ItemStack(egg));
                    world.addFreshEntity(itemEntity);
                });
    }

}