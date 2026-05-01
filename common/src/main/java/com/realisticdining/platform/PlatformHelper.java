package com.realisticdining.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PlatformHelper {
    
    @ExpectPlatform
    public static Platform getPlatform() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void openCookbookMenu(ServerPlayer player) {
        throw new AssertionError();
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.tryBuild("realisticdining", path);
    }

    public enum Platform {
        FABRIC,
        FORGE,
        NEOFORGE
    }
}
