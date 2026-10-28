package jaxvanyang.poker.entity;

import jaxvanyang.poker.item.Card;
import jaxvanyang.poker.item.CardDeck;
import jaxvanyang.poker.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CardEntity extends EntityBase {
    public static final EntityDataAccessor<Byte> CARD = SynchedEntityData.defineId(
            CardEntity.class,
            EntityDataSerializers.BYTE
    );

    public CardEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public CardEntity(Level level, Vec3 pos, float rotation, byte color, byte card) {
        super(Services.PLATFORM.getCardEntityType(), level);
        setPos(pos);
        setRotation(rotation);
        setColor(color);
        setCard(card);
    }

    public byte getCard() {
        return entityData.get(CARD);
    }

    public void setCard(byte card) {
        entityData.set(CARD, card);
    }

    public void pick(Player player, InteractionHand hand) {
        ItemStack stack = Card.newStack(getColor(), getCard());
        player.setItemInHand(hand, stack);
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 pos, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        // place the card over self
        if (heldStack.getItem() instanceof Card) {
            Level level = level();
            if (!level().isClientSide()) {
                CardEntity entity = new CardEntity(
                        level,
                        this.position().add(pos),
                        player.getYRot(),
                        Card.getColor(heldStack),
                        Card.getCardId(heldStack)
                );
                level.addFreshEntity(entity);
                heldStack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return super.interactAt(player, pos, hand);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (hand == InteractionHand.MAIN_HAND && heldStack.isEmpty()) {
            if (!level().isClientSide()) {
                pick(player, hand);
            }

            return InteractionResult.SUCCESS;
        }

        // pick the card and place it onto the deck
        if (heldStack.getItem() instanceof CardDeck cardDeck) {
            byte deckColor = cardDeck.getColor();
            byte[] deck = CardDeck.getDeck(heldStack);
            byte card = getCard();

            if (deckColor != getColor() || CardDeckEntity.is_card_in_deck(card, deck)) {
                return InteractionResult.PASS;
            }

            if (!level().isClientSide()) {
                byte[] newDeck = new byte[deck.length + 1];
                newDeck[0] = card;
                System.arraycopy(deck, 0, newDeck, 1, deck.length);
                CardDeck.setDeck(heldStack, newDeck);
                remove(RemovalReason.DISCARDED);
            }

            return InteractionResult.SUCCESS;
        }

        return super.interact(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                ItemStack stack = Card.newStack(getColor(), getCard());
                ItemEntity entity = new ItemEntity(level(), getX(), getY(), getZ(), stack);
                entity.setNoPickUpDelay();
                entity.setDeltaMovement(0, 0, 0);
                level().addFreshEntity(entity);
            }

            remove(RemovalReason.DISCARDED);

            return true;
        }

        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CARD, (byte) 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setCard(compoundTag.getByte("card"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putByte("card", getCard());
    }
}