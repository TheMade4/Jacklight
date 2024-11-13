package net.tm4.jacklight.mixin.common.world;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.tm4.jacklight.common.util.CoordinateUtils;
import net.tm4.jacklight.common.world.ExtendedWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level implements WorldGenLevel, ExtendedWorld {

    @Shadow
    @Final
    private ServerChunkCache chunkSource;

    protected ServerWorldMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
    }

    @Override
    public final LevelChunk getChunkAtImmediately(final int chunkX, final int chunkZ) {
        final ChunkMap storage = this.chunkSource.chunkMap;
        final ChunkHolder holder = storage.getVisibleChunkIfPresent(CoordinateUtils.getChunkKey(chunkX, chunkZ));

        if (holder == null) {
            return null;
        }

        return (LevelChunk) holder.getChunkIfPresentUnchecked(ChunkStatus.FULL);
    }

    @Override
    public final ChunkAccess getAnyChunkImmediately(final int chunkX, final int chunkZ) {
        final ChunkMap storage = this.chunkSource.chunkMap;
        final ChunkHolder holder = storage.getVisibleChunkIfPresent(CoordinateUtils.getChunkKey(chunkX, chunkZ));

        return holder == null ? null : holder.getLatestChunk();
    }
}
