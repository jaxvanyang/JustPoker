package jaxvanyang.poker.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityBase extends Entity {
    public static final EntityDataAccessor<Byte> COLOR = SynchedEntityData.defineId(
            EntityBase.class,
            EntityDataSerializers.BYTE
    );
    public static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(
            EntityBase.class,
            EntityDataSerializers.FLOAT
    );

    public static void spawnStackAtEntity(Level level, ItemStack stack, Entity entity) {
        ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), stack);
        itemEntity.setNoPickUpDelay();
        itemEntity.setDeltaMovement(0, 0, 0);
        level.addFreshEntity(itemEntity);
    }

    public EntityBase(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public byte getColor() {
        return entityData.get(COLOR);
    }

    public void setColor(byte color) {
        assert color >= 0;
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