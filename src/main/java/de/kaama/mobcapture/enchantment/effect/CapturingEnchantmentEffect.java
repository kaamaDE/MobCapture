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
import net.minecraft.util.math.random.Random;

public record CapturingEnchantmentEffect(EnchantmentLevelBasedValue dropChanceValue) implements EnchantmentEntityEffect {
    public static final MapCodec<CapturingEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EnchantmentLevelBasedValue.CODEC.fieldOf("drop_chance_value").forGetter(CapturingEnchantmentEffect::dropChanceValue)
            ).apply(instance, CapturingEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (context.owner() instanceof PlayerEntity player && target instanceof LivingEntity victim) {
            if (victim.isDead()) {
                if (isBossEntity(victim.getType())) {
                    return;
                }

                Random random = world.getRandom();
                float currentDropChance = dropChanceValue.getValue(level);

                if (random.nextFloat() < currentDropChance) {
                    trySpawnEgg(world, victim);
                }
            }
        }
    }

    /**
     * Helper method to determine if an EntityType is a "boss".
     */
    private boolean isBossEntity(EntityType<?> type) {
        return type.equals(EntityType.ENDER_DRAGON) ||
                type.equals(EntityType.WITHER) ||
                type.equals(EntityType.ELDER_GUARDIAN);
    }

    /**
     * Attempts to create and drop a spawn egg at the victim's location.
     * Only runs on the server side.
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