package net.themade4.jacklight.fabric.mixin.common.chunk;

import net.themade4.jacklight.fabric.common.chunk.ExtendedChunk;
import net.themade4.jacklight.fabric.common.light.SWMRNibbleArray;
import net.themade4.jacklight.fabric.common.light.StarLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyLevelChunk.class)
public abstract class EmptyLevelChunkMixin extends LevelChunk implements ExtendedChunk {

    public EmptyLevelChunkMixin(final Level level, final ChunkPos pos) {
        super(level, pos);
    }

    @Override
    public SWMRNibbleArray[] getBlockNibbles() {
        return StarLightEngine.getFilledEmptyLight(this.getLevel());
    }

    @Override
    public void setBlockNibbles(final SWMRNibbleArray[] nibbles) {}

    @Override
    public SWMRNibbleArray[] getSkyNibbles() {
        return StarLightEngine.getFilledEmptyLight(this.getLevel());
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
