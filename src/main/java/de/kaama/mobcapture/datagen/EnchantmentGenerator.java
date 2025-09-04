package de.kaama.mobcapture.datagen;

//? if >=1.21 {

import de.kaama.mobcapture.MobCaptureMod;
import de.kaama.mobcapture.enchantment.effect.CapturingEnchantmentEffect;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class EnchantmentGenerator extends FabricDynamicRegistryProvider {
    public EnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {

        // This block is active for Minecraft versions 1.21.2 and above.
        // It uses 'registries.getOrThrow(RegistryKeys.ITEM)'.
        /*? if >=1.21.2 {*/
        register(entries, MobCaptureMod.CAPTURING_ENCHANTMENT_KEY, Enchantment.builder(
                        Enchantment.definition(
                                registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.SWORDS),
                                registries.getOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.SWORDS),
                                10,
                                3,
                                Enchantment.leveledCost(1, 10),
                                Enchantment.leveledCost(1, 15),
                                5,
                                AttributeModifierSlot.HAND
                        ))
                .addEffect(
                        EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new CapturingEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.05f, 0.05f))
                )
        );
        /*?}*/
        // This block is active for Minecraft versions 1.21 and 1.21.1 only.
        // It uses 'registries.getWrapperOrThrow(RegistryKeys.ITEM)'.
        /*? if <1.21.2 {*/
        /*register(entries, MobCaptureMod.CAPTURING_ENCHANTMENT_KEY, Enchantment.builder(
                        Enchantment.definition(
                                registries.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.SWORDS),
                                registries.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemTags.SWORDS),
                                10,
                                3,
                                Enchantment.leveledCost(1, 10),
                                Enchantment.leveledCost(1, 15),
                                5,
                                AttributeModifierSlot.HAND
                        ))
                .addEffect(
                        EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new CapturingEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.05f, 0.05f))
                )
        );
        *//*?}*/
    }

    private void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions) {
        entries.add(key, builder.build(key.getValue()), resourceConditions);
    }

    @Override
    public String getName() {
        return MobCaptureMod.MOD_ID + " Enchantment Generator";
    }
}
//?}