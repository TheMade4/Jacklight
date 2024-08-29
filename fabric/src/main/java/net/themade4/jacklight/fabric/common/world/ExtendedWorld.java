package net.themade4.jacklight.fabric.common.world;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

public interface ExtendedWorld {

    // rets full chunk without blocking
    public LevelChunk getChunkAtImmediately(final int chunkX, final int chunkZ);

    // rets chunk at any stage, if it exists, immediately
    public ChunkAccess getAnyChunkImmediately(final int chunkX, final int chunkZ);

}
