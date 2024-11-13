package net.tm4.jacklight.fabric;

import net.tm4.jacklight.Jacklight;
import net.fabricmc.api.ModInitializer;

public final class JacklightFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Jacklight.init();
    }
}
