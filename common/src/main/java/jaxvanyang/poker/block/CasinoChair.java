package jaxvanyang.poker.block;

import jaxvanyang.poker.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CasinoChair extends Block {
    private static final VoxelShape SHAPE = Block.box(3.5, 0, 3.5, 12.5, 9, 12.5);

    private static final IntegerProperty FACING = IntegerProperty.create("facing", 0, 7);

    public CasinoChair(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(FACING, 0));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var player = context.getPlayer();
        if (player == null) {
            return super.getStateForPlacement(context);
        }

        var angle = player.getYRot();
        var facing = ((int) Math.floor((angle + 22.5) / 45)) & 7;

        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (!level.isClientSide()) {
            return SeatEntity.create(level, pos, 0.55, player, hitResult.getDirection());
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
