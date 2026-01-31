package com.wolfycz1;

import lombok.Getter;
import lombok.Setter;

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

    public enum UsageEffect {
        RESTORE_POWER("RESTORE_POWER");

        private final String usageEffectName;

        UsageEffect(String usageEffectName) {
            this.usageEffectName = usageEffectName;
        }

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
