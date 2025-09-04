package de.kaama.mobcapture.enchantment;

//? if >=1.21 {
import com.mojang.serialization.MapCodec;
import de.kaama.mobcapture.MobCaptureMod;
import de.kaama.mobcapture.enchantment.effect.CapturingEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MobEnchantmentRegistries {

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(MobCaptureMod.MOD_ID, id), codec);
    }

    public static final MapCodec<CapturingEnchantmentEffect> CAPTURING_EFFECT_CODEC = register("capturing_effect", CapturingEnchantmentEffect.CODEC);

    public static void registerEnchantmentComponents() {
        MobCaptureMod.LOGGER.info("Registering custom enchantment effect codecs for {}", MobCaptureMod.MOD_ID);
        MapCodec<?> test = CAPTURING_EFFECT_CODEC;
    }
}
//?}