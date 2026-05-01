package com.realisticdining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealisticDining {
    public static final String MOD_ID = "realisticdining";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("========================================");
        LOGGER.info("[Realistic Dining] Initializing...");
        LOGGER.info("========================================");
    }
}
