package net.themade4.jacklight.forge.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public final class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("jacklight.mixins.json");
    }
}