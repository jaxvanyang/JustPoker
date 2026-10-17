package jaxvanyang.poker.platform.services;

import jaxvanyang.poker.entity.CardDeckEntity;
import jaxvanyang.poker.entity.CardEntity;
import jaxvanyang.poker.entity.ChipEntity;
import jaxvanyang.poker.entity.SeatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public interface IPlatformHelper {
    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    default Item getCardItem() {
        throw new UnsupportedOperationException("getCardItem is not implemented for this platform");
    }

    default Item getCardBackItem() {
        throw new UnsupportedOperationException("getCardBackItem is not implemented for this platform");
    }

    default Item getCardDeckItem(byte color) {
        throw new UnsupportedOperationException("getCardDeckItem is not implemented for this platform");
    }

    default Item getChipItem(byte color) {
        throw new UnsupportedOperationException("getChipItem is not implemented for this platform");
    }

    default EntityType<SeatEntity> getSeatEntityType() {
        throw new UnsupportedOperationException("getSeatEntityType is not implemented for this platform");
    }

    default EntityType<CardDeckEntity> getCardDeckEntityType() {
        throw new UnsupportedOperationException("getCardDeckEntityType is not implemented for this platform");
    }

    default EntityType<CardEntity> getCardEntityType() {
        throw new UnsupportedOperationException("getCardEntityType is not implemented for this platform");
    }

    default EntityType<ChipEntity> getChipEntityType() {
        throw new UnsupportedOperationException("getChipEntityType is not implemented for this platform");
    }
}
