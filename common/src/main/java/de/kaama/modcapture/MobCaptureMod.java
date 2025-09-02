package de.kaama.modcapture;


import com.mojang.logging.LogUtils;

import de.kaama.modcapture.enchantment.CapturingEnchantment;
import dev.architectury.event.events.common.EntityEvent;
import org.slf4j.Logger;

public final class MobCaptureMod {

    public static final String MOD_ID = "mobcapture";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        EntityEvent.LIVING_DEATH.register(CapturingEnchantment::onEntityDeath);
    }

}