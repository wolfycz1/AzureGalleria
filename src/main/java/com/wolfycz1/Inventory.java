package com.wolfycz1;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Inventory {
    private final List<Item> items;
    private static final int CAPACITY = 2;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (isFull()) return false;
        return items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public boolean hasItem(String itemName) {
        return items.stream().anyMatch(i -> Objects.equals(i.getName().toLowerCase(), itemName.toLowerCase()));
    }

    public Item getItem(String itemName) {
        return items.stream().filter(i -> Objects.equals(i.getName().toLowerCase(), itemName.toLowerCase())).findFirst().orElse(null);
    }

    public String listItems() {
        if (items.isEmpty()) return "";
        else return items.stream().map(Item::getName).toList().toString();
    }

    private boolean isFull() {
        return items.size() >= CAPACITY;
    }

    public int getCapacity() {
        return CAPACITY;
    }
}
