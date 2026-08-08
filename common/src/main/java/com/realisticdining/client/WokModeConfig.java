package com.realisticdining.client;

public class WokModeConfig {
    
    private static boolean simplifiedMode = false;
    
    public static final int SIMPLIFIED_REQUIRED_STIRS = 4;
    public static final int NORMAL_REQUIRED_STIRS = 30;
    
    public static final long COOK_DURATION = 40 * 20;
    public static final long BURN_DURATION = 20 * 20;
    
    public static final long STAGE1_TIME = 15 * 20;
    public static final long STAGE2_TIME = 25 * 20;
    
    public static boolean isSimplifiedMode() {
        return simplifiedMode;
    }
    
    public static void enableSimplifiedMode() {
        simplifiedMode = true;
    }
    
    public static void disableSimplifiedMode() {
        simplifiedMode = false;
    }
    
    public static int getRequiredStirs() {
        return simplifiedMode ? SIMPLIFIED_REQUIRED_STIRS : NORMAL_REQUIRED_STIRS;
    }
    
    public static int getTimeBasedStage(long cookingStartTime, long currentGameTime) {
        if (cookingStartTime == 0) {
            return 0;
        }
        
        long elapsed = currentGameTime - cookingStartTime;
        
        if (elapsed >= STAGE2_TIME) {
            return 2;
        } else if (elapsed >= STAGE1_TIME) {
            return 1;
        }
        return 0;
    }
}
