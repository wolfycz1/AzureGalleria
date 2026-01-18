package com.wolfycz1;

import java.util.List;

public class Inventory {
    private List<Item> items;
    private static int CAPACITY = 2;

    public void addItem(Item item) {}

    public void removeItem(Item item) {}

    public boolean hasItem(String itemName) {
        return false;
    }

    public Item getItem(String itemName) {
        return null;
    }

    public String listItems() {
        return null;
    }

    private boolean isFull() {
        return false;
    }
}
