package com.example.realisticdining.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.example.realisticdining.init.ModItems;

public class PepperyChickenPlateBlock extends Block {

    public static final IntegerProperty PICKS = IntegerProperty.create("picks", 0, 35);
    public static final BooleanProperty HAS_CHOPSTICKS = BooleanProperty.create("has_chopsticks");
    public static final VoxelShape SHAPE = Shapes.box(2.0 / 16.0, 0.0, 2.0 / 16.0, 14.0 / 16.0, 3.0 / 16.0, 14.0 / 16.0);
    
    private static final int SPEED_TICKS_PER_BITE = 60;
    private static final int SPEED_FINISH_BONUS = 300;
    private static final int SPEED_MAX_DURATION = 2400;
    private static final int STRENGTH_TICKS_PER_BITE = 137;
    private static final int STRENGTH_FINISH_BONUS = 1200;
    private static final int STRENGTH_MAX_DURATION = 6000;
    private static final int HASTE_TICKS_PER_BITE = 154;
    private static final int HASTE_FINISH_BONUS = 1200;
    private static final int HASTE_MAX_DURATION = 6600;

    public PepperyChickenPlateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PICKS, 0).setValue(HAS_CHOPSTICKS, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PICKS, HAS_CHOPSTICKS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        int picks = state.getValue(PICKS);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);
        if (picks == 0 || picks == 35 || hasChopsticks) {
            return 1.0f;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        int currentPicks = state.getValue(PICKS);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (currentPicks == 35) {
            if (hasChopsticks) {
                if (!level.isClientSide) {
                    if (heldItem.isEmpty() || heldItem.is(Items.AIR)) {
                        player.setItemInHand(hand, new ItemStack(ModItems.CHOPSTICKS.get()));
                        level.setBlock(pos, state.setValue(HAS_CHOPSTICKS, false), 3);
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
                    } else {
                        popResource(level, pos, new ItemStack(Items.BOWL));
                        popResource(level, pos, new ItemStack(ModItems.CHOPSTICKS.get()));
                        level.destroyBlock(pos, false);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                if (heldItem.is(ModItems.CHOPSTICKS.get())) {
                    if (!level.isClientSide) {
                        if (!player.getAbilities().instabuild) {
                            heldItem.shrink(1);
                        }
                        level.setBlock(pos, state.setValue(HAS_CHOPSTICKS, true), 3);
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            return InteractionResult.PASS;
        }
        
        if (heldItem.is(ModItems.CHOPSTICKS.get())) {
            if (currentPicks < 35) {
                if (!level.isClientSide) {
                    int newPicks = currentPicks + 1;
                    level.setBlock(pos, state.setValue(PICKS, newPicks), 3);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                    
                    applyBuff(player, newPicks == 35);
                    
                    ItemStack foodOnChopsticks = getChopsticksWithFood(newPicks);
                    player.setItemInHand(hand, foodOnChopsticks);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return InteractionResult.PASS;
    }
    
    private void applyBuff(Player player, boolean isFinished) {
        int speedDuration = SPEED_TICKS_PER_BITE;
        int strengthDuration = STRENGTH_TICKS_PER_BITE;
        int hasteDuration = HASTE_TICKS_PER_BITE;
        
        if (isFinished) {
            speedDuration += SPEED_FINISH_BONUS;
            strengthDuration += STRENGTH_FINISH_BONUS;
            hasteDuration += HASTE_FINISH_BONUS;
        }
        
        addOrExtendEffect(player, MobEffects.MOVEMENT_SPEED, speedDuration, 0, SPEED_MAX_DURATION);
        addOrExtendEffect(player, MobEffects.DAMAGE_BOOST, strengthDuration, 0, STRENGTH_MAX_DURATION);
        addOrExtendEffect(player, MobEffects.DIG_SPEED, hasteDuration, 0, HASTE_MAX_DURATION);
    }
    
    private void addOrExtendEffect(Player player, net.minecraft.world.effect.MobEffect effect, int duration, int amplifier, int maxDuration) {
        MobEffectInstance currentEffect = player.getEffect(effect);
        if (currentEffect != null) {
            duration = Math.min(duration + currentEffect.getDuration(), maxDuration);
        }
        player.addEffect(new MobEffectInstance(effect, duration, amplifier, true, true));
    }
    
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        
        int picks = state.getValue(PICKS);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);
        
        if (!level.isClientSide) {
            if (picks == 0) {
                popResource(level, pos, new ItemStack(ModItems.PEPPERY_CHICKEN.get()));
            } else if (picks == 35 || hasChopsticks) {
                popResource(level, pos, new ItemStack(Items.BOWL));
                if (hasChopsticks) {
                    popResource(level, pos, new ItemStack(ModItems.CHOPSTICKS.get()));
                }
            }
        }
    }
    
    private ItemStack getChopsticksWithFood(int pickNumber) {
        return switch (pickNumber) {
            case 1 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_1.get());
            case 2 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_2.get());
            case 3 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_3.get());
            case 4 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_4.get());
            case 5 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_5.get());
            case 6 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_6.get());
            case 7 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_7.get());
            case 8 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_8.get());
            case 9 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_9.get());
            case 10 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_10.get());
            case 11 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_11.get());
            case 12 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_12.get());
            case 13 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_13.get());
            case 14 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_14.get());
            case 15 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_15.get());
            case 16 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_16.get());
            case 17 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_17.get());
            case 18 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_18.get());
            case 19 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_19.get());
            case 20 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_20.get());
            case 21 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_21.get());
            case 22 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_22.get());
            case 23 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_23.get());
            case 24 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_24.get());
            case 25 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_25.get());
            case 26 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_26.get());
            case 27 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_27.get());
            case 28 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_28.get());
            case 29 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_29.get());
            case 30 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_30.get());
            case 31 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_31.get());
            case 32 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_32.get());
            case 33 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_33.get());
            case 34 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_34.get());
            case 35 -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_35.get());
            default -> new ItemStack(ModItems.CHOPSTICKS_PEPPERY_CHICKEN_1.get());
        };
    }
}
