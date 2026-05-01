package com.realisticdining.blocks;

import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

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
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        int currentPicks = state.getValue(PICKS);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (currentPicks == 35 && hasChopsticks) {
            if (!level.isClientSide) {
                ItemStack mainHandItem = player.getMainHandItem();
                if (mainHandItem.isEmpty()) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.CHOPSTICKS.get()));
                    level.setBlock(pos, state.setValue(HAS_CHOPSTICKS, false), 3);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
                } else {
                    popResource(level, pos, new ItemStack(Items.BOWL));
                    popResource(level, pos, new ItemStack(ModItems.CHOPSTICKS.get()));
                    level.destroyBlock(pos, false);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int currentPicks = state.getValue(PICKS);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (currentPicks == 35 && !hasChopsticks && stack.is(ModItems.CHOPSTICKS.get())) {
            if (!level.isClientSide) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.setBlock(pos, state.setValue(HAS_CHOPSTICKS, true), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (stack.is(ModItems.CHOPSTICKS.get()) && currentPicks < 35) {
            if (!level.isClientSide) {
                int newPicks = currentPicks + 1;
                level.setBlock(pos, state.setValue(PICKS, newPicks), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                
                applyBuff(player, newPicks == 35);
                
                ItemStack foodOnChopsticks = new ItemStack(ModItems.getChopsticksPepperyChicken(newPicks).get());
                player.setItemInHand(hand, foodOnChopsticks);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
    
    private void addOrExtendEffect(Player player, net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect, int duration, int amplifier, int maxDuration) {
        MobEffectInstance currentEffect = player.getEffect(effect);
        if (currentEffect != null) {
            duration = Math.min(duration + currentEffect.getDuration(), maxDuration);
        }
        player.addEffect(new MobEffectInstance(effect, duration, amplifier, true, true));
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        
        int picks = state.getValue(PICKS);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (!level.isClientSide) {
            if (picks == 0) {
                ItemStack dish = new ItemStack(ModItems.PEPPERY_CHICKEN.get());
                popResource(level, pos, dish);
            } else if (picks == 35 || hasChopsticks) {
                popResource(level, pos, new ItemStack(Items.BOWL));
                if (hasChopsticks) {
                    popResource(level, pos, new ItemStack(ModItems.CHOPSTICKS.get()));
                }
            }
        }
    }
}
