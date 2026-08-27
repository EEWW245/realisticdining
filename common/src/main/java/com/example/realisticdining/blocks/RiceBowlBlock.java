package com.example.realisticdining.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

import com.example.realisticdining.init.ModItems;
import com.example.realisticdining.compat.KaleidoscopeCompat;
import com.example.realisticdining.items.PlaceableFoodItem;

public class RiceBowlBlock extends Block {
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 15);
    public static final BooleanProperty HAS_CHOPSTICKS = BooleanProperty.create("has_chopsticks");
    
    private static final VoxelShape SHAPE = Shapes.or(
        Shapes.box(5.0/16.0, 0.0, 5.0/16.0, 11.0/16.0, 1.0/16.0, 11.0/16.0),
        Shapes.box(4.0/16.0, 1.0/16.0, 4.0/16.0, 12.0/16.0, 4.0/16.0, 5.0/16.0),
        Shapes.box(4.0/16.0, 1.0/16.0, 11.0/16.0, 12.0/16.0, 4.0/16.0, 12.0/16.0),
        Shapes.box(4.0/16.0, 1.0/16.0, 5.0/16.0, 5.0/16.0, 4.0/16.0, 11.0/16.0),
        Shapes.box(11.0/16.0, 1.0/16.0, 5.0/16.0, 12.0/16.0, 4.0/16.0, 11.0/16.0),
        Shapes.box(5.0/16.0, 4.0/16.0, 5.0/16.0, 11.0/16.0, 7.0/16.0, 11.0/16.0)
    );
    
    public RiceBowlBlock(Properties properties) {
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
        if (bites == 0 || bites == 15 || hasChopsticks) {
            return 1.0f;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        int currentBites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);

        if (currentBites == 15) {
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
            if (currentBites < 15) {
                if (!level.isClientSide) {
                    int newBites = currentBites + 1;
                    level.setBlock(pos, state.setValue(BITES, newBites), 3);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                    
                    applyBite(player);
                    
                    ItemStack riceOnChopsticks = new ItemStack(ModItems.CHOPSTICKS_RICE.get());
                    player.setItemInHand(hand, riceOnChopsticks);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * 每咬一口恢复 1 点饱食度（v2.2.5）。
     * 原 SATURATION 药水效果会瞬间拉满饱食度与饱和度，一咬全满不合理；
     * 改为直接走 FoodData：每口 +1 food + 0.2 饱和度，整碗 16 口吃完。
     */
    private void applyBite(Player player) {
        player.getFoodData().eat(1, 0.2f);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        
        int bites = state.getValue(BITES);
        boolean hasChopsticks = state.getValue(HAS_CHOPSTICKS);
        
        if (!level.isClientSide) {
            if (bites == 15 || hasChopsticks) {
                popResource(level, pos, new ItemStack(Items.BOWL));
                if (hasChopsticks) {
                    popResource(level, pos, new ItemStack(ModItems.CHOPSTICKS.get()));
                }
            } else if (bites == 0) {
                ItemStack compatRice = KaleidoscopeCompat.getRiceItem();
                if (!compatRice.isEmpty()) {
                    popResource(level, pos, compatRice);
                } else {
                    popResource(level, pos, new ItemStack(ModItems.RICE_BOWL.get()));
                }
            } else {
                ItemStack riceItem = new ItemStack(ModItems.RICE_BOWL.get());
                PlaceableFoodItem.setBitesToStack(riceItem, "RiceBowlBites", bites);
                popResource(level, pos, riceItem);
            }
        }
    }
}
