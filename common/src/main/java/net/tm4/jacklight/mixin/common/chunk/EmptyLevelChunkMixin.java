package net.tm4.jacklight.mixin.common.chunk;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.tm4.jacklight.common.chunk.ExtendedChunk;
import net.tm4.jacklight.common.light.JackLightEngine;
import net.tm4.jacklight.common.light.SWMRNibbleArray;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyLevelChunk.class)
public abstract class EmptyLevelChunkMixin extends LevelChunk implements ExtendedChunk {

    public EmptyLevelChunkMixin(final Level level, final ChunkPos pos) {
        super(level, pos);
    }

    @Override
    public SWMRNibbleArray[] getBlockNibbles() {
        return JackLightEngine.getFilledEmptyLight(this.getLevel());
    }

    @Override
    public void setBlockNibbles(final SWMRNibbleArray[] nibbles) {}

    @Override
    public SWMRNibbleArray[] getSkyNibbles() {
        return JackLightEngine.getFilledEmptyLight(this.getLevel());
    }

    @Override
    public void setSkyNibbles(final SWMRNibbleArray[] nibbles) {}

    @Override
    public boolean[] getSkyEmptinessMap() {
        return null;
    }

    @Override
    public void setSkyEmptinessMap(final boolean[] emptinessMap) {}

    @Override
    public boolean[] getBlockEmptinessMap() {
        return null;
    }

    @Override
    public void setBlockEmptinessMap(final boolean[] emptinessMap) {}
}
