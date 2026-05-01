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

public class StirFriedPorkCabbagePlateBlock extends Block {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 29);
    public static final BooleanProperty HAS_CHOPSTICKS = BooleanProperty.create("has_chopsticks");
    public static final VoxelShape SHAPE = Shapes.box(2.0 / 16.0, 0.0, 2.0 / 16.0, 14.0 / 16.0, 3.0 / 16.0, 14.0 / 16.0);
    
    private static final int STRENGTH_TICKS_PER_BITE = 160;
    private static final int STRENGTH_FINISH_BONUS = 600;
    private static final int STRENGTH_MAX_DURATION = 3000;
    private static final int REGEN_TICKS_PER_BITE = 41;
    private static final int REGEN_MAX_DURATION = 1200;

    public StirFriedPorkCabbagePlateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 29).setValue(HAS_CHOPSTICKS, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES, HAS_CHOPSTICKS);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        int bites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);
        if (bites == 0 || hasChopsticks) {
            return 1.0f;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        int currentBites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (currentBites == 0 && hasChopsticks) {
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
        int currentBites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (currentBites == 0 && !hasChopsticks && stack.is(ModItems.CHOPSTICKS.get())) {
            if (!level.isClientSide) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.setBlock(pos, state.setValue(HAS_CHOPSTICKS, true), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (stack.is(ModItems.CHOPSTICKS.get()) && currentBites > 0) {
            if (!level.isClientSide) {
                int newBites = currentBites - 1;
                level.setBlock(pos, state.setValue(BITES, newBites), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                
                applyBuff(player, newBites == 0);
                
                ItemStack foodOnChopsticks;
                if (currentBites == 29 || currentBites == 28 || currentBites == 2 || currentBites == 1) {
                    foodOnChopsticks = new ItemStack(ModItems.CHOPSTICKS_CABBAGE.get());
                } else {
                    foodOnChopsticks = new ItemStack(ModItems.CHOPSTICKS_PORK.get());
                }
                
                player.setItemInHand(hand, foodOnChopsticks);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    
    private void applyBuff(Player player, boolean isFinished) {
        int strengthDuration = STRENGTH_TICKS_PER_BITE;
        int regenDuration = REGEN_TICKS_PER_BITE;
        
        if (isFinished) {
            strengthDuration += STRENGTH_FINISH_BONUS;
        }
        
        addOrExtendEffect(player, MobEffects.DAMAGE_BOOST, strengthDuration, 0, STRENGTH_MAX_DURATION);
        addOrExtendEffect(player, MobEffects.REGENERATION, regenDuration, 0, REGEN_MAX_DURATION);
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
        
        int bites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (!level.isClientSide) {
            if (bites == 29) {
                ItemStack dish = new ItemStack(ModItems.STIR_FRIED_PORK_CABBAGE.get());
                popResource(level, pos, dish);
            } else if (bites == 0 || hasChopsticks) {
                popResource(level, pos, new ItemStack(Items.BOWL));
                if (hasChopsticks) {
                    popResource(level, pos, new ItemStack(ModItems.CHOPSTICKS.get()));
                }
            }
        }
    }
}
