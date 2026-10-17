package jaxvanyang.poker.entity;

import jaxvanyang.poker.item.Card;
import jaxvanyang.poker.item.CardDeck;
import jaxvanyang.poker.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
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

import java.util.Arrays;

public class CardDeckEntity extends CardBase {
    public static final EntityDataSerializer<byte[]> BYTE_ARRAY = EntityDataSerializer.forValueType(ByteBufCodecs.BYTE_ARRAY);
    public static final EntityDataAccessor<byte[]> DECK = SynchedEntityData.defineId(CardDeckEntity.class, BYTE_ARRAY);

    public CardDeckEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public CardDeckEntity(Level level, Vec3 pos, float rotation, byte color, byte[] deck) {
        super(Services.PLATFORM.getCardDeckEntityType(), level);
        setPos(pos);
        setColor(color);
        setRotation(rotation);
        setDeck(deck);
    }

    public static boolean is_card_in_deck(byte cardId, byte[] deck) {
        for (byte i : deck) {
            if (i == cardId) {
                return true;
            }
        }
        return false;
    }

    public byte[] getDeck() {
        return entityData.get(DECK);
    }

    public void setDeck(byte[] deck) {
        entityData.set(DECK, deck);
    }

    public void deal(Player player, InteractionHand hand) {
        byte color = getColor();
        byte[] deck = getDeck();
        ItemStack card = Card.newStack(color, deck[0]);
        player.setItemInHand(hand, card);
        byte[] newDeck = Arrays.copyOfRange(deck, 1, deck.length);
        setDeck(newDeck);
    }

    public void placeBack(Player player, InteractionHand hand, byte cardId) {
        byte[] deck = getDeck();
        byte[] newDeck = new byte[deck.length + 1];
        newDeck[0] = cardId;
        System.arraycopy(deck, 0, newDeck, 1, deck.length);
        setDeck(newDeck);
        player.setItemInHand(hand, ItemStack.EMPTY);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        // shuffle the deck only when it's full
        if (player.isCrouching()) {
            if (getDeck().length == CardDeck.DECK_SIZE) {
                if (!level().isClientSide()) {
                    setDeck(CardDeck.newRandomDeck());
                } else {
                    // TODO: play shuffle sound
                    player.displayClientMessage(Component.literal("Deck is shuffled"), true);
                }

                return InteractionResult.SUCCESS;
            } else if (level().isClientSide()) {
                player.displayClientMessage(Component.literal("Deck is not full, cannot shuffle"), true);
            }

            return InteractionResult.PASS;
        }

        // place card back to deck
        if (heldStack.getItem() instanceof Card card) {
            byte cardColor = Card.getColor(heldStack);
            assert 0 <= cardColor && cardColor < 4;
            byte cardId = Card.getCardId(heldStack);
            assert 1 <= cardId && cardId <= CardDeck.DECK_SIZE;
            if (cardColor != getColor() || is_card_in_deck(cardId, getDeck())) {
                return InteractionResult.PASS;
            }

            if (!level().isClientSide()) {
                placeBack(player, hand, cardId);
            }

            return InteractionResult.SUCCESS;
        }

        // pick the top card
        if (hand == InteractionHand.MAIN_HAND && heldStack.isEmpty()) {
            if (!level().isClientSide()) {
                deal(player, hand);
            }

            return InteractionResult.SUCCESS;
        }

        return super.interact(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                ItemStack stack = CardDeck.newStack(getColor(), getDeck());
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
    public void tick() {
        if (getDeck().length == 0) {
            remove(RemovalReason.DISCARDED);
            return;
        }

        super.tick();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DECK, new byte[0]);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setDeck(compoundTag.getByteArray("deck"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putByteArray("deck", getDeck());
    }
}