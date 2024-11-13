package net.tm4.jacklight.common.blockstate;

public interface ExtendedAbstractBlockState {
    public boolean isConditionallyFullOpaque();
    public int getOpacityIfCached();
}
