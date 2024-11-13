package net.tm4.jacklight.neoforge;

import net.tm4.jacklight.Jacklight;
import net.neoforged.fml.common.Mod;

@Mod(Jacklight.MOD_ID)
public final class JacklightNeoForge {
    public JacklightNeoForge() {
        // Run our common setup.
        Jacklight.init();
    }
}
