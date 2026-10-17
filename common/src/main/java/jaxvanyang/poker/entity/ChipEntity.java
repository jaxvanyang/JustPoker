package jaxvanyang.poker.entity;

import jaxvanyang.poker.item.Chip;
import jaxvanyang.poker.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ChipEntity extends EntityBase {
    public static final byte MAX_SIZE = 64;

    public static final EntityDataAccessor<Byte> SIZE = SynchedEntityData.defineId(
            ChipEntity.class,
            EntityDataSerializers.BYTE
    );

    public ChipEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ChipEntity(Level level, Vec3 pos, float rotation, byte color) {
        super(Services.PLATFORM.getChipEntityType(), level);
        setPos(pos);
        setRotation(rotation);
        setColor(color);
        setSize((byte) 1);
    }

    public byte getSize() {
        return entityData.get(SIZE);
    }

    public void setSize(byte size) {
        assert 0 <= size && size <= MAX_SIZE;
        entityData.set(SIZE, size);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        // place chip
        if (heldStack.getItem() instanceof Chip chip && chip.getColor() == getColor()) {
            if (getSize() >= MAX_SIZE) {
                if (level().isClientSide()) {
                    player.displayClientMessage(Component.literal("The stack is full!"), true);
                }

                return InteractionResult.CONSUME;
            }

            if (!level().isClientSide()) {
                setSize((byte) (getSize() + 1));
                heldStack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        // pick chip
        if (!level().isClientSide()) {
            spawnStackAtEntity(level(), Chip.newStack(getColor()), player);
            setSize((byte) (getSize() - 1));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player) {
            if (!player.isCreative() && !level().isClientSide()) {
                ItemStack stack = Chip.newStack(getColor());
                stack.setCount(getSize());
                spawnStackAtEntity(level(), stack, player);
            }

            remove(RemovalReason.DISCARDED);
            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (getSize() <= 0) {
            remove(RemovalReason.DISCARDED);
            return;
        }

        // FIXME: don't update bounding box here
        Vec3 pos = position();
        double margin = 0.125D;
        double dy = 0.01575D;
        setBoundingBox(new AABB(
                pos.x - margin,
                pos.y,
                pos.z - margin,
                pos.x + margin,
                pos.y + 0.02D + (dy * getSize()),
                pos.z + margin
        ));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SIZE, (byte) 1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setSize(compoundTag.getByte("size"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putByte("size", getSize());
    }
}
