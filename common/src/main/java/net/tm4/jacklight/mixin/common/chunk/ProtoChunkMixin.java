package net.tm4.jacklight.mixin.common.chunk;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;
import net.tm4.jacklight.common.chunk.ExtendedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProtoChunk.class)
public abstract class ProtoChunkMixin implements ExtendedChunk {

    /**
     * @reason Remove unused skylight sources
     * @author Spottedleaf
     */
    @Redirect(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/lighting/ChunkSkyLightSources;update(Lnet/minecraft/world/level/BlockGetter;III)Z"
            )
    )
    private boolean skipLightSources(final ChunkSkyLightSources instance, final BlockGetter blockGetter,
                                     final int x, final int y, final int z) {
        return false;
    }
}
