package jaxvanyang.poker.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CardBase extends Entity {
    public static final EntityDataAccessor<Byte> COLOR = SynchedEntityData.defineId(
            CardBase.class,
            EntityDataSerializers.BYTE
    );
    public static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(
            CardBase.class,
            EntityDataSerializers.FLOAT
    );

    public CardBase(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public byte getColor() {
        return entityData.get(COLOR);
    }

    public void setColor(byte color) {
        entityData.set(COLOR, color);
    }

    public float getRotation() {
        return entityData.get(ROTATION);
    }

    public void setRotation(float rotation) {
        entityData.set(ROTATION, rotation);
    }

    @Override
    public void tick() {
        super.tick();

        // Apply gravity
        if (isNoGravity()) {
            return;
        }
        Vec3 v = getDeltaMovement();
        v = v.add(0.0, -0.06, 0.0);
        move(MoverType.SELF, v);
    }

    // TODO: implement pickable
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(COLOR, (byte) 0);
        builder.define(ROTATION, 0.0F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        setColor(compoundTag.getByte("color"));
        setRotation(compoundTag.getFloat("rotation"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putByte("color", getColor());
        compoundTag.putFloat("rotation", getRotation());
    }
}