package jaxvanyang.poker.platform;

import jaxvanyang.poker.Poker;
import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.entity.ChipEntity;
import jaxvanyang.poker.entity.SeatEntity;
import jaxvanyang.poker.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Item getCardItem() {
        return Poker.CARD;
    }

    @Override
    public Item getCardBackItem() {
        return Poker.CARD_BACK;
    }

    @Override
    public Item getCardDeckItem(byte color) {
        return switch (color) {
            case 1 -> Poker.BLUE_CARD_DECK;
            case 2 -> Poker.PINK_CARD_DECK;
            case 3 -> Poker.RED_CARD_DECK;
            default -> Poker.BLACK_CARD_DECK;
        };
    }

    @Override
    public Item getChipItem(byte color) {
        return switch (color) {
            case 0 -> Poker.BLACK_CASINO_CHIP;
            case 1 -> Poker.BLUE_CASINO_CHIP;
            case 2 -> Poker.GREEN_CASINO_CHIP;
            case 3 -> Poker.RED_CASINO_CHIP;
            case 4 -> Poker.WHITE_CASINO_CHIP;
            default -> throw new IllegalArgumentException("Invalid color: " + color);
        };
    }

    @Override
    public EntityType<SeatEntity> getSeatEntityType() {
        return Poker.SEAT;
    }

    @Override
    public EntityType<CardDeckEntity> getCardDeckEntityType() {
        return Poker.CARD_DECK_TYPE;
    }

    @Override
    public EntityType<CardEntity> getCardEntityType() {
        return Poker.CARD_TYPE;
    }

    @Override
    public EntityType<ChipEntity> getChipEntityType() {
        return Poker.CHIP_TYPE;
    }
}
