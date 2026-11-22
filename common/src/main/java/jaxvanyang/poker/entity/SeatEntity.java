package jaxvanyang.poker.entity;

import jaxvanyang.poker.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SeatEntity extends Entity {
    public SeatEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    private SeatEntity(Level level, BlockPos pos, double dy, Direction dir) {
        this(Services.PLATFORM.getSeatEntityType(), level);
        this.setPos(pos.getX() + 0.5, pos.getY() + dy, pos.getZ() + 0.5);
        this.setRot(dir.toYRot(), 0.0f);
    }

    public static InteractionResult create(Level level, BlockPos pos, double dy, Player player, Direction direction) {
        if (!level.isClientSide()) {
            SeatEntity seat = new SeatEntity(level, pos, dy, direction);
            level.addFreshEntity(seat);
            player.startRiding(seat, false);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public void tick() {
        super.tick();

        Level level = level();
        if (level.isClientSide()) {
            return;
        }

        if (getPassengers().isEmpty() || level.isEmptyBlock(blockPosition())) {
            remove(RemovalReason.DISCARDED);
            level.updateNeighbourForOutputSignal(blockPosition(), level.getBlockState(blockPosition()).getBlock());
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return true;
    }
}
