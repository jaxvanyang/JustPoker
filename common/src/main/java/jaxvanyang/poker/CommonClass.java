package jaxvanyang.poker;

import jaxvanyang.poker.platform.Services;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("just_poker")) {
            Constants.LOG.info("Just Poker mod is loaded");
        }
    }
}
