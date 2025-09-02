package de.kaama.modcapture.neoforge;

import de.kaama.modcapture.MobCaptureMod;
import net.neoforged.fml.common.Mod;

@Mod(MobCaptureMod.MOD_ID)
public final class ModCaptureModNeoForge {
    public ModCaptureModNeoForge() {
        // Run our common setup.
        MobCaptureMod.init();
    }
}
