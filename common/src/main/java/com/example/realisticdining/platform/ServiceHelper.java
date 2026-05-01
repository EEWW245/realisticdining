package com.example.realisticdining.platform;

import java.util.ServiceLoader;

public class ServiceHelper {
    private static PlatformServices PLATFORM_SERVICES;
    
    public static PlatformServices getPlatformServices() {
        if (PLATFORM_SERVICES == null) {
            PLATFORM_SERVICES = ServiceLoader.load(PlatformServices.class)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No PlatformServices implementation found!"));
        }
        return PLATFORM_SERVICES;
    }
}
