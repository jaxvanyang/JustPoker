package jaxvanyang.poker.entity;

import jaxvanyang.poker.item.Chip;
import jaxvanyang.poker.platform.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ChipEntity extends EntityBase {
    public static final byte MAX_SIZE = 64;

    public static final EntityDataAccessor<Byte> SIZE = SynchedEntityData.defineId(
            ChipEntity.class,
            EntityDataSerializers.BYTE
    );

    public ChipEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ChipEntity(Level level, Vec3 pos, float rotation, byte color, byte size) {
        super(Services.PLATFORM.getChipEntityType(), level);
        setPos(pos);
        setRotation(rotation);
        setColor(color);
        setSize(size);
    }

    public byte getSize() {
        return entityData.get(SIZE);
    }

    public void setSize(byte size) {
        if (size < 0) {
            size = 0;
        } else if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }
        if (size == 0) {
            remove(RemovalReason.DISCARDED);
            return;
        }
        entityData.set(SIZE, size);
        refreshDimensions();
        setCustomName(Component.literal(String.valueOf(size)));
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(0.25F, 0.02F + 0.01575F * getSize());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        var heldStack = player.getItemInHand(hand);

        // place chip
        if (heldStack.getItem() instanceof Chip chip && chip.getColor() == getColor()) {
            var chipCount = getSize();
            if (chipCount >= MAX_SIZE) {
                if (level().isClientSide()) {
                    player.sendOverlayMessage(Component.translatable("message.just_poker.chip_stack_is_full"));
                }
                return InteractionResult.CONSUME;
            }

            var placeCount = 1;
            if (player.isCrouching()) {
                placeCount = heldStack.getCount();
                if (placeCount + chipCount > MAX_SIZE) {
                    placeCount = MAX_SIZE - chipCount;
                }
            }

            if (!level().isClientSide()) {
                setSize((byte) (placeCount + chipCount));
                heldStack.shrink(placeCount);
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
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                ItemStack stack = Chip.newStack(getColor());
                stack.setCount(getSize());
                spawnStackAtEntity(level, stack, player);
            }

            remove(RemovalReason.DISCARDED);
            return true;
        }

        return false;
    }

    @Override
    public void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> dataValues) {
        super.onSyncedDataUpdated(dataValues);

        for (var dataValue : dataValues) {
            if (dataValue.id() == SIZE.id()) {
                refreshDimensions();
                break;
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SIZE, (byte) 1);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        setSize(valueInput.getByteOr("size", (byte) 1));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putByte("size", getSize());
    }
}
