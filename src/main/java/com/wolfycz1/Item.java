package com.wolfycz1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Item {
    private String name;
    private String description;
    private boolean pickupable;
}
