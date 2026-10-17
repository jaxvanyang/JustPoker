package jaxvanyang.poker.platform;

import jaxvanyang.poker.Poker;
import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.entity.ChipEntity;
import jaxvanyang.poker.entity.SeatEntity;
import jaxvanyang.poker.platform.services.IPlatformHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Item getCardItem() {
        return Poker.CARD.get();
    }

    @Override
    public Item getCardBackItem() {
        return Poker.CARD_BACK.get();
    }

    @Override
    public Item getCardDeckItem(byte color) {
        return switch (color) {
            case 1 -> Poker.BLUE_CARD_DECK.get();
            case 2 -> Poker.PINK_CARD_DECK.get();
            case 3 -> Poker.RED_CARD_DECK.get();
            default -> Poker.BLACK_CARD_DECK.get();
        };
    }

    @Override
    public Item getChipItem(byte color) {
        return switch (color) {
            case 0 -> Poker.BLACK_CASINO_CHIP.get();
            case 1 -> Poker.BLUE_CASINO_CHIP.get();
            case 2 -> Poker.GREEN_CASINO_CHIP.get();
            case 3 -> Poker.RED_CASINO_CHIP.get();
            case 4 -> Poker.WHITE_CASINO_CHIP.get();
            default -> throw new IllegalArgumentException("Invalid color: " + color);
        };
    }

    @Override
    public EntityType<SeatEntity> getSeatEntityType() {
        return Poker.SEAT.get();
    }

    @Override
    public EntityType<CardDeckEntity> getCardDeckEntityType() {
        return Poker.CARD_DECK_TYPE.get();
    }

    @Override
    public EntityType<CardEntity> getCardEntityType() {
        return Poker.CARD_TYPE.get();
    }

    @Override
    public EntityType<ChipEntity> getChipEntityType() {
        return Poker.CHIP_TYPE.get();
    }
}
