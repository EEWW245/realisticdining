package com.example.realisticdining.blocks;

import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

public class FriedRiceEggBlock extends Block {
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 10);
    public static final BooleanProperty HAS_CHOPSTICKS = BooleanProperty.create("has_chopsticks");
    
    private static final VoxelShape SHAPE = Shapes.or(
        Shapes.box(5.0/16.0, 0.0, 5.0/16.0, 11.0/16.0, 1.0/16.0, 11.0/16.0),
        Shapes.box(4.0/16.0, 1.0/16.0, 4.0/16.0, 12.0/16.0, 4.0/16.0, 5.0/16.0),
        Shapes.box(4.0/16.0, 1.0/16.0, 11.0/16.0, 12.0/16.0, 4.0/16.0, 12.0/16.0),
        Shapes.box(4.0/16.0, 1.0/16.0, 5.0/16.0, 5.0/16.0, 4.0/16.0, 11.0/16.0),
        Shapes.box(11.0/16.0, 1.0/16.0, 5.0/16.0, 12.0/16.0, 4.0/16.0, 11.0/16.0),
        Shapes.box(5.0/16.0, 4.0/16.0, 5.0/16.0, 11.0/16.0, 7.0/16.0, 11.0/16.0)
    );
    
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        int currentBites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (currentBites == 10) {
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
            if (currentBites < 10) {
                if (!level.isClientSide) {
                    int newBites = currentBites + 1;
                    level.setBlock(pos, state.setValue(BITES, newBites), 3);
                    player.getFoodData().eat(2, 0.3f);
                    level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);

                    applyBuff(player, newBites == 10);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return InteractionResult.PASS;
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
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        
        int bites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);
        
        if (!level.isClientSide) {
            if (bites == 0) {
                Item eggFriedRice = KaleidoscopeCompat.getEggFriedRice();
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
