package de.kaama.mobcapture;

import net.fabricmc.api.ModInitializer;
//? if <1.21 {
// import net.minecraft.enchantment.Enchantment;
// import net.minecraft.enchantment.EnchantmentTarget;
// import net.minecraft.entity.EquipmentSlot;
// import net.minecraft.registry.Registries;
// import net.minecraft.registry.Registry;
//?}
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if >=1.21 {
import de.kaama.mobcapture.enchantment.MobEnchantmentRegistries;
//?}


public class MobCaptureMod implements ModInitializer {
    public static final String MOD_ID = "mobcapture";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String VERSION = /*$ mod_version*/ "0.1.1";
    public static final String MINECRAFT = /*$ minecraft*/ "1.21.8";

    public static final RegistryKey<net.minecraft.enchantment.Enchantment> CAPTURING_ENCHANTMENT_KEY = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "capturing"));

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing MobCaptureMod - Version: {}", VERSION);

        //? if <1.21 {
        // TODO: Implement pre 1.21 enchantment registration
        //?} else {
        MobEnchantmentRegistries.registerEnchantmentComponents();
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