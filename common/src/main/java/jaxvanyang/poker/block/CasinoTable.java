package jaxvanyang.poker.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CasinoTable extends Block {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 15, 16);

    private static final BooleanProperty NORTH = BooleanProperty.create("north");
    private static final BooleanProperty EAST = BooleanProperty.create("east");
    private static final BooleanProperty SOUTH = BooleanProperty.create("south");
    private static final BooleanProperty WEST = BooleanProperty.create("west");

    private static final BooleanProperty NORTHWEST = BooleanProperty.create("northwest");
    private static final BooleanProperty NORTHEAST = BooleanProperty.create("northeast");
    private static final BooleanProperty SOUTHWEST = BooleanProperty.create("southwest");
    private static final BooleanProperty SOUTHEAST = BooleanProperty.create("southeast");


    public CasinoTable(Properties properties) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

        registerDefaultState(getStateDefinition().any()
                                     .setValue(NORTH, false)
                                     .setValue(EAST, false)
                                     .setValue(SOUTH, false)
                                     .setValue(WEST, false)
                                     .setValue(NORTHWEST, false)
                                     .setValue(NORTHEAST, false)
                                     .setValue(SOUTHWEST, false)
                                     .setValue(SOUTHEAST, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        return getState(level, pos);
    }

    private BlockState getState(LevelAccessor level, BlockPos pos) {
        return defaultBlockState().setValue(NORTH, canConnectTo(level, pos, NORTH))
                .setValue(EAST, canConnectTo(level, pos, EAST))
                .setValue(SOUTH, canConnectTo(level, pos, SOUTH))
                .setValue(WEST, canConnectTo(level, pos, WEST))
                .setValue(NORTHWEST, canConnectTo(level, pos, NORTHWEST))
                .setValue(NORTHEAST, canConnectTo(level, pos, NORTHEAST))
                .setValue(SOUTHWEST, canConnectTo(level, pos, SOUTHWEST))
                .setValue(SOUTHEAST, canConnectTo(level, pos, SOUTHEAST));
    }

    private boolean canConnectTo(LevelAccessor level, BlockPos pos, BooleanProperty dir) {
        var dir_str = dir.toString();
        if (dir_str.contains("north")) {
            pos = pos.north();
        } else if (dir_str.contains("south")) {
            pos = pos.south();
        }
        if (dir_str.contains("east")) {
            pos = pos.east();
        } else if (dir_str.contains("west")) {
            pos = pos.west();
        }

        var block = level.getBlockState(pos).getBlock();

        return block instanceof CasinoTable;
    }
}
