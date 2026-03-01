package com.wolfycz1;

import com.wolfycz1.models.Inventory;
import com.wolfycz1.models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void addItem_NullItem_NoItemFailure() {
        // Arrange
        // Act
        Optional<Inventory.InventoryFailure> failure = inventory.addItem(null);
        // Assert
        assertTrue(failure.isPresent());
        assertEquals(Inventory.InventoryFailure.NO_ITEM, failure.get());
    }

    @Test
    void addItem_ItemNotPickupable_NotPickupableFailure() {
        // Arrange
        Item item = new Item("name", "description", false, null);
        // Act
        Optional<Inventory.InventoryFailure> failure = inventory.addItem(item);
        // Assert
        assertTrue(failure.isPresent());
        assertEquals(Inventory.InventoryFailure.NOT_PICKUPABLE, failure.get());
    }

    @Test
    void addItem_InventoryFull_InventoryFullFailure() {
        // Arrange
        Item item = new Item("name", "description", true, null);
        fillToCapacity();
        // Act
        Optional<Inventory.InventoryFailure> failure = inventory.addItem(item);
        // Assert
        assertTrue(failure.isPresent());
        assertEquals(Inventory.InventoryFailure.INVENTORY_FULL, failure.get());
    }

    @Test
    void addItem_ValidItem_EmptyFailure() {
        // Arrange
        Item item = new Item("name", "description", true, null);
        // Act
        Optional<Inventory.InventoryFailure> failure = inventory.addItem(item);
        // Assert
        assertTrue(failure.isEmpty());
    }

    @Test
    void removeItem_NullName_EmptyOptional() {
        // Arrange
        // Act
        Optional<Item> item = inventory.removeItem(null);
        // Assert
        assertTrue(item.isEmpty());
    }

    @Test
    void removeItem_NotInInventory_EmptyOptional() {
        // Arrange
        // Act
        Optional<Item> item = inventory.removeItem("item");
        // Assert
        assertTrue(item.isEmpty());
    }

    @Test
    void removeItem_ValidItem_Item() {
        // Arrange
        Item addedItem = new Item("name", "description", true, null);
        inventory.addItem(addedItem);
        // Act
        Optional<Item> removedItem = inventory.removeItem(addedItem.getName());
        // Assert
        assertTrue(removedItem.isPresent());
        assertEquals(addedItem, removedItem.get());
    }

    @Test
    void removeItem_DifferentCase_Item() {
        // Arrange
        Item addedItem = new Item("NAme", "description", true, null);
        inventory.addItem(addedItem);
        // Act
        Optional<Item> removedItem = inventory.removeItem("naME");
        // Assert
        assertTrue(removedItem.isPresent());
        assertEquals(addedItem, removedItem.get());
    }

    @Test
    void removeItem_EmptyName_EmptyOptional() {
        // Arrange
        // Act
        Optional<Item> item = inventory.removeItem("");
        // Assert
        assertTrue(item.isEmpty());
    }

    @Test
    void getItem_ValidName_Item() {
        // Arrange
        Item addedItem = new Item("name", "description", true, null);
        inventory.addItem(addedItem);
        // Act
        Optional<Item> gotItem = inventory.getItem(addedItem.getName());
        // Assert
        assertTrue(gotItem.isPresent());
        assertEquals(addedItem, gotItem.get());
    }

    @Test
    void getItem_DifferentCase_Item() {
        // Arrange
        Item addedItem = new Item("name", "description", true, null);
        inventory.addItem(addedItem);
        // Act
        Optional<Item> gotItem = inventory.getItem("nAmE");
        // Assert
        assertTrue(gotItem.isPresent());
        assertEquals(addedItem, gotItem.get());
    }

    @Test
    void getItem_EmptyName_EmptyOptional() {
        // Arrange
        // Act
        Optional<Item> gotItem = inventory.getItem("");
        // Assert
        assertTrue(gotItem.isEmpty());
    }

    @Test
    void getItem_NullName_EmptyOptiona() {
        // Arrange
        // Act
        Optional<Item> gotItem = inventory.getItem(null);
        // Assert
        assertTrue(gotItem.isEmpty());
    }

    @Test
    void getItem_NotInInventory_EmptyOptional() {
        // Arrange
        // Act
        Optional<Item> item = inventory.getItem("item");
        // Assert
        assertTrue(item.isEmpty());
    }

    private void fillToCapacity() {
        for (int i = 0; i < inventory.getCapacity(); i++) {
            inventory.addItem(new Item(String.format("item%d", i), "description", true, null));
        }
    }
}