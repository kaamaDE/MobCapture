package de.kaama.mobcapture;

import net.fabricmc.api.ModInitializer;
//? if <1.21 {
// import net.minecraft.enchantment.Enchantment;
// import net.minecraft.enchantment.EnchantmentTarget;
// import net.minecraft.entity.EquipmentSlot;
// import net.minecraft.registry.Registries;
// import net.minecraft.registry.Registry;
//?}
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if >=1.21 {
import com.mojang.serialization.MapCodec;
import de.kaama.mobcapture.enchantment.effect.CapturingEnchantmentEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
//?}


public class MobCaptureMod implements ModInitializer {

    public static final String MOD_ID = "mobcapture";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String VERSION = /*$ mod_version*/ "0.1.3";
    public static final String MINECRAFT = /*$ minecraft*/ "1.21.8";
    public static final RegistryKey<Enchantment> CAPTURING = RegistryKey.of(RegistryKeys.ENCHANTMENT, id(MOD_ID, "capturing"));
    public static final MapCodec<CapturingEnchantmentEffect> CAPTURING_EFFECT = Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, id(MOD_ID, "capturing_effect"), CapturingEnchantmentEffect.CODEC);

    @Override
    public void onInitialize() {
        //? if <1.21 {
        // TODO: Implement pre 1.21 enchantment registration
        //?}
    }

    /**
     * Adapts to the {@link Identifier} changes introduced in 1.21.
     */
    public static Identifier id(String namespace, String path) {
        //? if <1.21 {
        /*return new Identifier(namespace, path);
         *///?} else
        return Identifier.of(namespace, path);
    }

}