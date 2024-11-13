package net.tm4.jacklight.common.world;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

public interface ExtendedWorld {
    public LevelChunk getChunkAtImmediately(final int chunkX, final int chunkZ);
    public ChunkAccess getAnyChunkImmediately(final int chunkX, final int chunkZ);
}
