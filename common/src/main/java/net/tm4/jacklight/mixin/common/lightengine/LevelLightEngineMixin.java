package net.tm4.jacklight.mixin.common.lightengine;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.LayerLightEventListener;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.lighting.LightEventListener;
import net.tm4.jacklight.common.chunk.ExtendedChunk;
import net.tm4.jacklight.common.light.JackLightEngine;
import net.tm4.jacklight.common.light.JackLightInterface;
import net.tm4.jacklight.common.light.JackLightLightingProvider;
import net.tm4.jacklight.common.light.SWMRNibbleArray;
import net.tm4.jacklight.common.util.CoordinateUtils;
import net.tm4.jacklight.common.util.WorldUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLightEngine.class)
public abstract class LevelLightEngineMixin implements LightEventListener, JackLightLightingProvider {

    @Shadow
    @Nullable
    private LightEngine<?, ?> blockEngine;

    @Shadow
    @Nullable
    private LightEngine<?, ?> skyEngine;

    @Unique
    protected JackLightInterface lightEngine;

    @Override
    public final JackLightInterface getLightEngine() {
        return this.lightEngine;
    }

    /**
     *
     * TODO since this is a constructor inject, check on update for new constructors
     */
    @Inject(
            method = "<init>", at = @At("TAIL")
    )
    public void construct(final LightChunkGetter chunkProvider, final boolean hasBlockLight, final boolean hasSkyLight,
                          final CallbackInfo ci) {
        // avoid ClassCastException in cases where custom LightChunkGetters do not return a Level from getLevel()
        if (chunkProvider.getLevel() instanceof Level) {
            this.lightEngine = new JackLightInterface(chunkProvider, hasSkyLight, hasBlockLight, (LevelLightEngine)(Object)this);
        } else {
            this.lightEngine = new JackLightInterface(null, hasSkyLight, hasBlockLight, (LevelLightEngine)(Object)this);
        }
        // intentionally destroy mods hooking into old light engine state
        this.blockEngine = null;
        this.skyEngine = null;
    }

    /**
     * @reason Route to new light engine
     * @author Spottedleaf
     */
    @Overwrite
    public void checkBlock(final BlockPos pos) {
        BlockPos a = pos.immutable();
        this.lightEngine.blockChange(a);
    }

    /**
     * @reason Route to new light engine
     * @author Spottedleaf
     */
    @Overwrite
    public boolean hasLightWork() {
        // route to new light engine
        return this.lightEngine.hasUpdates();
    }

    /**
     * @reason Hook into new light engine for light updates
     * @author Spottedleaf
     */
    @Overwrite
    public int runLightUpdates() {
        // replace impl
        final boolean hadUpdates = this.hasLightWork();
        this.lightEngine.propagateChanges();
        return hadUpdates ? 1 : 0;
    }

    /**
     * @reason New light engine hook for handling empty section changes
     * @author Spottedleaf
     */
    @Overwrite
    public void updateSectionStatus(final SectionPos pos, final boolean notReady) {
        this.lightEngine.sectionChange(pos, notReady);
    }

    /**
     * @reason Avoid messing with the vanilla light engine state
     * @author Spottedleaf
     */
    @Overwrite
    public void setLightEnabled(final ChunkPos pos, final boolean lightEnabled) {
        // not invoked by the client
    }

    /**
     * @reason Avoid messing with the vanilla light engine state
     * @author Spottedleaf
     */
    @Overwrite
    public void propagateLightSources(ChunkPos param0) {
        // not invoked by the client
    }

    /**
     * @reason Replace light views with our own that hook into the new light engine instead of vanilla's
     * @author Spottedleaf
     */
    @Overwrite
    public LayerLightEventListener getLayerListener(final LightLayer lightType) {
        return lightType == LightLayer.BLOCK ? this.lightEngine.getBlockReader() : this.lightEngine.getSkyReader();
    }

    /**
     * @reason Avoid messing with the vanilla light engine state
     * @author Spottedleaf
     */
    @Overwrite
    public void queueSectionData(final LightLayer lightType, final SectionPos pos, @Nullable final DataLayer nibble) {
        // do not allow modification of data from the non-chunk load hooks
    }

    /**
     * @reason Avoid messing with the vanilla light engine state
     * @author Spottedleaf
     */
    @Overwrite
    public String getDebugData(final LightLayer lightType, final SectionPos pos) {
        // TODO would be nice to make use of this
        return "n/a";
    }

    /**
     * @reason Avoid messing with the vanilla light engine state
     * @author Spottedleaf
     */
    @Overwrite
    public void retainData(final ChunkPos pos, final boolean retainData) {
        // not used by new light impl
    }

    /**
     * @reason Need to use our own hooks for retrieving light data
     * @author Spottedleaf
     */
    @Overwrite
    public int getRawBrightness(final BlockPos pos, final int ambientDarkness) {
        // need to use new light hooks for this
        return this.lightEngine.getRawBrightness(pos, ambientDarkness);
    }

    /**
     * @reason Need to use our own hooks for checking this state
     * @author Spottedleaf
     */
    @Overwrite
    public boolean lightOnInSection(final SectionPos pos) {
        final long key = CoordinateUtils.getChunkKey(pos.getX(), pos.getZ());
        return (!this.lightEngine.hasBlockLight() || this.blockLightMap.get(key) != null) && (!this.lightEngine.hasSkyLight() || this.skyLightMap.get(key) != null);
    }

    @Unique
    protected final Long2ObjectOpenHashMap<SWMRNibbleArray[]> blockLightMap = new Long2ObjectOpenHashMap<>();

    @Unique
    protected final Long2ObjectOpenHashMap<SWMRNibbleArray[]> skyLightMap = new Long2ObjectOpenHashMap<>();

    @Override
    public void clientUpdateLight(final LightLayer lightType, final SectionPos pos,
                                  final DataLayer nibble, final boolean trustEdges) {
        if (((Object)this).getClass() != LevelLightEngine.class) {
            throw new IllegalStateException("This hook is for the CLIENT ONLY");
        }
        // data storage changed with new light impl
        final ChunkAccess chunk = this.getLightEngine().getAnyChunkNow(pos.getX(), pos.getZ());
        switch (lightType) {
            case BLOCK: {
                final SWMRNibbleArray[] blockNibbles = this.blockLightMap.computeIfAbsent(CoordinateUtils.getChunkKey(pos), (final long keyInMap) -> {
                    return JackLightEngine.getFilledEmptyLight(this.lightEngine.getWorld());
                });

                blockNibbles[pos.getY() - WorldUtil.getMinLightSection(this.lightEngine.getWorld())] = SWMRNibbleArray.fromVanilla(nibble);

                if (chunk != null) {
                    ((ExtendedChunk)chunk).setBlockNibbles(blockNibbles);
                    this.lightEngine.getLightAccess().onLightUpdate(LightLayer.BLOCK, pos);
                }
                break;
            }
            case SKY: {
                final SWMRNibbleArray[] skyNibbles = this.skyLightMap.computeIfAbsent(CoordinateUtils.getChunkKey(pos), (final long keyInMap) -> {
                    return JackLightEngine.getFilledEmptyLight(this.lightEngine.getWorld());
                });

                skyNibbles[pos.getY() - WorldUtil.getMinLightSection(this.lightEngine.getWorld())] = SWMRNibbleArray.fromVanilla(nibble);

                if (chunk != null) {
                    ((ExtendedChunk)chunk).setSkyNibbles(skyNibbles);
                    this.lightEngine.getLightAccess().onLightUpdate(LightLayer.SKY, pos);
                }
                break;
            }
        }
    }

    @Override
    public void clientRemoveLightData(final ChunkPos chunkPos) {
        if (((Object)this).getClass() != LevelLightEngine.class) {
            throw new IllegalStateException("This hook is for the CLIENT ONLY");
        }
        this.blockLightMap.remove(CoordinateUtils.getChunkKey(chunkPos));
        this.skyLightMap.remove(CoordinateUtils.getChunkKey(chunkPos));
    }

    @Override
    public void clientChunkLoad(final ChunkPos pos, final LevelChunk chunk) {
        if (((Object)this).getClass() != LevelLightEngine.class) {
            throw new IllegalStateException("This hook is for the CLIENT ONLY");
        }
        final long key = CoordinateUtils.getChunkKey(pos);
        final SWMRNibbleArray[] blockNibbles = this.blockLightMap.get(key);
        final SWMRNibbleArray[] skyNibbles = this.skyLightMap.get(key);
        if (blockNibbles != null) {
            ((ExtendedChunk)chunk).setBlockNibbles(blockNibbles);
        }
        if (skyNibbles != null) {
            ((ExtendedChunk)chunk).setSkyNibbles(skyNibbles);
        }
    }
}
