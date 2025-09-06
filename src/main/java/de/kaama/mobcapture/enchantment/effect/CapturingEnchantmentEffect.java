package de.kaama.mobcapture.enchantment.effect;

//? if >=1.21 {

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.kaama.mobcapture.MobCaptureMod;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record CapturingEnchantmentEffect(
        EnchantmentLevelBasedValue dropChanceValue) implements EnchantmentEntityEffect {
    public static final MapCodec<CapturingEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(EnchantmentLevelBasedValue.CODEC.fieldOf("drop_chance_value").forGetter(CapturingEnchantmentEffect::dropChanceValue)).apply(instance, CapturingEnchantmentEffect::new));

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (!(context.owner() instanceof PlayerEntity && target instanceof LivingEntity victim && victim.isDead()))
            return;

        if (victim instanceof PlayerEntity || isBossEntity(victim.getType()))
            return;

        var random = world.getRandom();
        var baseDropChance = dropChanceValue.getValue(level);

        var difficulty = world.getDifficulty();
        var difficultyMultiplier = switch (difficulty) {
            case PEACEFUL -> 1.5f;
            case EASY -> 1.25f;
            case NORMAL -> 1.0f;
            case HARD -> 0.75f;
        };

        var spawnGroup = victim.getType().getSpawnGroup();
        var spawnGroupMultiplier = switch (spawnGroup) {
            case MONSTER -> 1.0f;
            case CREATURE, WATER_CREATURE, AXOLOTLS, UNDERGROUND_WATER_CREATURE -> 0.6f;
            case AMBIENT, WATER_AMBIENT -> 0.35f;
            case MISC -> 0.2f;
        };

        var adjustedDropChance = Math.clamp(baseDropChance * difficultyMultiplier * spawnGroupMultiplier, 0.005f, 0.20f);

        if (random.nextFloat() < adjustedDropChance)
            trySpawnEgg(world, victim);
    }

    /**
     * Helper method to determine if an EntityType is a "boss".
     */
    private boolean isBossEntity(EntityType<?> type) {
        return type.equals(EntityType.ENDER_DRAGON) || type.equals(EntityType.WITHER) || type.equals(EntityType.ELDER_GUARDIAN);
    }

    /**
     * Attempts to create and drop a spawn egg at the victim's location.
     */
    private void trySpawnEgg(ServerWorld world, LivingEntity victim) {
        if (world.isClient()) return;

        SpawnEggItem spawnEgg = SpawnEggItem.forEntity(victim.getType());

        if (spawnEgg != null) {
            ItemStack spawnEggStack = new ItemStack(spawnEgg);
            ItemEntity itemEntity = new ItemEntity(world, victim.getX(), victim.getY(), victim.getZ(), spawnEggStack);
            world.spawnEntity(itemEntity);
        } else {
            MobCaptureMod.LOGGER.debug("No spawn egg found for entity type: {}", victim.getType().getName().getString());
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
//?}