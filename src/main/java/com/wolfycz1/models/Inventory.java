package com.wolfycz1.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a container for in-game items with a fixed maximum capacity.
 * @author wolfycz1
 */
public class Inventory {
    private final List<Item> items;
    private static final int CAPACITY = 2;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    /**
     * Represents the specific reasons why adding an item to the inventory might fail.
     */
    public enum InventoryFailure {
        NOT_PICKUPABLE,
        INVENTORY_FULL,
        NO_ITEM,
    }

    /**
     * Attempts to add an item to the inventory.
     * @param item The Item object to be added.
     * @return Empty Optional if the addition was successful or an Optional of {@link InventoryFailure}
     * detailing why the operation was rejected.
     */
    public Optional<InventoryFailure> addItem(Item item) {
        if (item == null) return Optional.of(InventoryFailure.NO_ITEM);
        if (isFull()) return Optional.of(InventoryFailure.INVENTORY_FULL);
        if (!item.isPickupable()) return Optional.of(InventoryFailure.NOT_PICKUPABLE);
        items.add(item);
        return Optional.empty();
    }

    /**
     * Removes an item from the inventory by its name (case-insensitive).
     * @param itemName The name of the item to remove.
     * @return An Optional containing the removed {@link Item} if successful, or an empty {@link Optional}
     * if not found in the inventory.
     */
    public Optional<Item> removeItem(String itemName) {
        Optional<Item> item = getItem(itemName);
        item.ifPresent(items::remove);
        return item;
    }

    /**
     * Searches for an item in the inventory by its name (case-insensitive).
     * @param itemName The name of the item to search for.
     * @return An Optional containing the Item if found, or an empty Optional if not found.
     */
    public Optional<Item> getItem(String itemName) {
        if (itemName == null) return Optional.empty();
        return items.stream().filter(i -> i.getName().equalsIgnoreCase(itemName)).findFirst();
    }

    /**
     * Returns a string representation of the items currently in the inventory.
     * @return A formatted string of item names or an empty string if the inventory is empty.
     */
    public String listItems() {
        if (items.isEmpty()) return "";
        else return items.stream().map(Item::getName).toList().toString();
    }

    /**
     * Checks if the inventory has reached its maximum capacity.
     * @return true if the inventory is full.
     */
    private boolean isFull() {
        return items.size() >= CAPACITY;
    }

    /**
     * Gets the static maximum number of items this inventory can hold.
     * @return The maximum capacity integer.
     */
    public int getCapacity() {
        return CAPACITY;
    }
}
