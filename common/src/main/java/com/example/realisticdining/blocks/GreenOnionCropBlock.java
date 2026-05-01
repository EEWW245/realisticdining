package com.example.realisticdining.blocks;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.example.realisticdining.init.ModItems;

public class GreenOnionCropBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
        Block.box(6.0, 0.0, 6.0, 10.0, 7.0, 10.0),
        Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0)
    };

    public GreenOnionCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 1;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.GREEN_ONION_SEEDS.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = state.getValue(AGE);
            if (age < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, level, pos);
                if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0) {
                    level.setBlock(pos, state.setValue(AGE, age + 1), 2);
                }
            }
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int newAge = Math.min(state.getValue(AGE) + 1, this.getMaxAge());
        level.setBlock(pos, state.setValue(AGE, newAge), 2);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int age = state.getValue(AGE);
        
        if (age == this.getMaxAge()) {
            if (!level.isClientSide) {
                int count = 1 + level.random.nextInt(2);
                popResource(level, pos, new ItemStack(ModItems.GREEN_ONION.get(), count));
                
                level.setBlock(pos, state.setValue(AGE, 0), 2);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        return InteractionResult.PASS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        int age = state.getValue(AGE);
        
        if (age == this.getMaxAge()) {
            java.util.List<ItemStack> drops = new java.util.ArrayList<>();
            drops.add(new ItemStack(ModItems.GREEN_ONION.get()));
            drops.add(new ItemStack(ModItems.GREEN_ONION_SEEDS.get()));
            return drops;
        } else {
            return java.util.List.of(new ItemStack(ModItems.GREEN_ONION_SEEDS.get()));
        }
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return 1;
    }
}
