package com.wolfycz1;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an interactable object within the game.
 * @author wolfycz1
 */
@Getter
@Setter
public class Item {
    private String name;
    private String description;
    private boolean pickupable;
    private Room unlocksRoom;
    private UsageEffect usageEffect;

    public Item(String name, String description, boolean pickupable, UsageEffect usageEffect) {
        this.name = name;
        this.description = description;
        this.pickupable = pickupable;
        this.usageEffect = usageEffect;
    }

    /**
     * Defines the specific effects that an item can trigger when used.
     */
    public enum UsageEffect {
        RESTORE_POWER("RESTORE_POWER");

        private final String usageEffectName;

        UsageEffect(String usageEffectName) {
            this.usageEffectName = usageEffectName;
        }

        /**
         * Parses a string into its corresponding UsageEffect enum value.
         * @param usageEffectName The string to parse.
         * @return The matching UsageEffect, or null if no match is found.
         */
        public static UsageEffect fromString(String usageEffectName) {
            for (UsageEffect usageEffect : UsageEffect.values()) {
                if (usageEffect.usageEffectName.equalsIgnoreCase(usageEffectName)) {
                    return usageEffect;
                }
            }
            return null;
        }
    }
}
