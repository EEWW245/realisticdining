package com.realisticdining.blocks;

import com.realisticdining.compat.KaleidoscopeCookeryCompat;
import com.realisticdining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FriedRiceEggBlock extends Block {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 10);
    public static final BooleanProperty HAS_CHOPSTICKS = BooleanProperty.create("has_chopsticks");
    public static final VoxelShape SHAPE = Shapes.box(2.0 / 16.0, 0.0, 2.0 / 16.0, 14.0 / 16.0, 3.0 / 16.0, 14.0 / 16.0);
    
    private static final int TICKS_PER_BITE = 210;
    private static final int FINISH_BONUS_TICKS = 300;
    private static final int MAX_DURATION = 2400;

    public FriedRiceEggBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0).setValue(HAS_CHOPSTICKS, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES, HAS_CHOPSTICKS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        int bites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);
        if (bites == 0 || bites == 10 || hasChopsticks) {
            return 1.0f;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        int currentBites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (currentBites == 10 && hasChopsticks) {
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

        if (currentBites == 10 && !hasChopsticks && stack.is(ModItems.CHOPSTICKS.get())) {
            if (!level.isClientSide) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.setBlock(pos, state.setValue(HAS_CHOPSTICKS, true), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (stack.is(ModItems.CHOPSTICKS.get()) && currentBites < 10) {
            if (!level.isClientSide) {
                int newBites = currentBites + 1;
                level.setBlock(pos, state.setValue(BITES, newBites), 3);
                player.getFoodData().eat(2, 0.3f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);

                applyBuff(player, newBites == 10);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    
    private void applyBuff(Player player, boolean isFinished) {
        int duration = TICKS_PER_BITE;
        if (isFinished) {
            duration += FINISH_BONUS_TICKS;
        }
        
        MobEffectInstance currentEffect = player.getEffect(MobEffects.SATURATION);
        if (currentEffect != null) {
            duration = Math.min(duration + currentEffect.getDuration(), MAX_DURATION);
        }
        
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, duration, 1, true, true));
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        int bites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (!level.isClientSide) {
            if (bites == 0) {
                Item eggFriedRice = KaleidoscopeCookeryCompat.getEggFriedRice();
                if (eggFriedRice != null) {
                    popResource(level, pos, new ItemStack(eggFriedRice));
                } else {
                    popResource(level, pos, new ItemStack(ModItems.FRIED_RICE_EGG.get()));
                }
            } else if (bites == 10 || hasChopsticks) {
                popResource(level, pos, new ItemStack(Items.BOWL));
                if (hasChopsticks) {
                    popResource(level, pos, new ItemStack(ModItems.CHOPSTICKS.get()));
                }
            }
        }
    }
}
