package net.tm4.jacklight.common.chunk;

import net.tm4.jacklight.common.light.SWMRNibbleArray;

public interface ExtendedChunk {
    public SWMRNibbleArray[] getBlockNibbles();
    public void setBlockNibbles(final SWMRNibbleArray[] nibbles);

    public SWMRNibbleArray[] getSkyNibbles();
    public void setSkyNibbles(final SWMRNibbleArray[] nibbles);

    public boolean[] getSkyEmptinessMap();
    public void setSkyEmptinessMap(final boolean[] emptinessMap);

    public boolean[] getBlockEmptinessMap();
    public void setBlockEmptinessMap(final boolean[] emptinessMap);
}
