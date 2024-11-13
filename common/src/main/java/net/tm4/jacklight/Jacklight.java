package net.tm4.jacklight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Jacklight {
    public static final String MOD_ID = "jacklight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Jacklight mod is loaded!");
    }
}
