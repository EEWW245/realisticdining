package com.realisticdining.neoforge.client.arm;

public class EatRiceState {

    private static EatRiceState INSTANCE;

    public static final int TOTAL_BITES = 9;
    private static final double[] ANIMATION_LENGTHS = {1.3545, 1.3823, 1.2935, 1.3218, 1.3554, 1.316, 1.3083, 1.3889, 1.7641};

    private int currentBiteIndex = 0;
    private double animationStartTime = -1;
    private boolean shouldConsumeRice = false;
    private boolean isAnimationPlaying = false;
    private boolean isEating = false;
    private boolean shouldPlayEatSound = false;
    private boolean shouldApplySaturation = false;

    private static final String[] ANIMATION_NAMES = {
        "animation.left_arm_fp.new",
        "animation.left_arm_fp.new2",
        "animation.left_arm_fp.new3",
        "animation.left_arm_fp.new4",
        "animation.left_arm_fp.new5",
        "animation.left_arm_fp.new6",
        "animation.left_arm_fp.new7",
        "animation.left_arm_fp.new8",
        "animation.left_arm_fp.new9"
    };

    private static final String[] RIGHT_ARM_ANIMATION_NAMES = {
        "animation.right_arm_fp.new",
        "animation.right_arm_fp.new2",
        "animation.right_arm_fp.new3",
        "animation.right_arm_fp.new4",
        "animation.right_arm_fp.new5",
        "animation.right_arm_fp.new6",
        "animation.right_arm_fp.new7",
        "animation.right_arm_fp.new8",
        "animation.right_arm_fp.new9"
    };

    private EatRiceState() {}

    public static EatRiceState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EatRiceState();
        }
        return INSTANCE;
    }

    public int getCurrentBiteIndex() {
        return currentBiteIndex;
    }

    public boolean isFinished() {
        return currentBiteIndex >= TOTAL_BITES;
    }

    public void startBite(double gameTime) {
        this.animationStartTime = gameTime;
        this.isAnimationPlaying = true;
        this.isEating = true;
    }

    public boolean shouldConsumeRice() {
        return shouldConsumeRice;
    }

    public void onRiceConsumed() {
        shouldConsumeRice = false;
    }

    public boolean isAnimationPlaying() {
        return isAnimationPlaying;
    }

    public boolean isEating() {
        return isEating;
    }

    public boolean shouldPlayEatSound() {
        return shouldPlayEatSound;
    }

    public void onEatSoundPlayed() {
        shouldPlayEatSound = false;
    }

    public boolean shouldApplySaturation() {
        return shouldApplySaturation;
    }

    public void onSaturationApplied() {
        shouldApplySaturation = false;
    }

    public void update(double currentGameTime) {
        if (animationStartTime >= 0 && currentBiteIndex < TOTAL_BITES) {
            double elapsed = (currentGameTime - animationStartTime) / 20.0;
            double animLength = ANIMATION_LENGTHS[currentBiteIndex];
            
            if (elapsed >= animLength) {
                currentBiteIndex++;
                animationStartTime = -1;
                isAnimationPlaying = false;
                shouldPlayEatSound = true;
                shouldApplySaturation = true;
                
                if (currentBiteIndex >= TOTAL_BITES) {
                    shouldConsumeRice = true;
                }
            }
        }
    }

    public void reset() {
        currentBiteIndex = 0;
        animationStartTime = -1;
        shouldConsumeRice = false;
        isAnimationPlaying = false;
        isEating = false;
        shouldPlayEatSound = false;
        shouldApplySaturation = false;
    }

    public String getLeftArmAnimationName() {
        if (currentBiteIndex >= TOTAL_BITES) {
            return null;
        }
        return ANIMATION_NAMES[currentBiteIndex];
    }

    public String getRightArmAnimationName() {
        if (currentBiteIndex >= TOTAL_BITES) {
            return null;
        }
        return RIGHT_ARM_ANIMATION_NAMES[currentBiteIndex];
    }

    public String getAnimationName() {
        return getLeftArmAnimationName();
    }

    public double getCurrentAnimationLength() {
        if (currentBiteIndex >= TOTAL_BITES) {
            return 0;
        }
        return ANIMATION_LENGTHS[currentBiteIndex];
    }
}
