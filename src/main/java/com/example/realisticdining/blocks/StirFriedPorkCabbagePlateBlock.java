package com.example.realisticdining.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;

import com.example.realisticdining.init.ModItems;

public class StirFriedPorkCabbagePlateBlock extends Block {

    // 0 到 29 口，29代表满盘，0代表空盘
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 29);
    public static final VoxelShape SHAPE = Shapes.box(2.0 / 16.0, 0.0, 2.0 / 16.0, 14.0 / 16.0, 3.0 / 16.0, 14.0 / 16.0);

    public StirFriedPorkCabbagePlateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 29));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        
        // 检查玩家是否拿着空筷子
        if (heldItem.is(ModItems.CHOPSTICKS.get())) {
            int currentBites = state.getValue(BITES);
            
            if (currentBites > 0) {
                if (!level.isClientSide) {
                    // 扣除一口
                    level.setBlock(pos, state.setValue(BITES, currentBites - 1), 3);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                    
                    // 判断夹到的是什么
                    ItemStack foodOnChopsticks;
                    if (currentBites == 29 || currentBites == 28 || currentBites == 2 || currentBites == 1) {
                        foodOnChopsticks = new ItemStack(ModItems.CHOPSTICKS_CABBAGE.get());
                    } else {
                        foodOnChopsticks = new ItemStack(ModItems.CHOPSTICKS_PORK.get());
                    }
                    
                    // 把手里的空筷子替换成夹着菜的筷子
                    player.setItemInHand(hand, foodOnChopsticks);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return InteractionResult.PASS;
    }
}
