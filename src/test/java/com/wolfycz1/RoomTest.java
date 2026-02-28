package com.wolfycz1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    Room room;
    Room setExit;

    @BeforeEach
    void setUp() {
        room = new Room("name", List.of("a", "b"), "description", "hint", false);
        setExit = new Room("exitName", List.of("c", "d"), "description", "hint", false);
        room.setExit("exit", setExit);
    }

    @Test
    void getExit_ValidName_Room() {
        // Arrange
        // Act
        Optional<Room> getExit = room.getExit(setExit.getName());
        // Assert
        assertTrue(getExit.isPresent());
        assertEquals(setExit, getExit.get());
    }

    @Test
    void getExit_ValidAlias_Room() {
        // Arrange
        // Act
        Optional<Room> getExit = room.getExit(setExit.getAliases().getFirst());
        // Assert
        assertTrue(getExit.isPresent());
        assertEquals(setExit, getExit.get());
    }

    @Test
    void getExit_DifferentCase_Room() {
        // Arrange
        // Act
        Optional<Room> getExit = room.getExit("ExItNaMe");
        // Assert
        assertTrue(getExit.isPresent());
        assertEquals(setExit, getExit.get());
    }

    @Test
    void getExit_InvalidName_EmptyOptional() {
        // Arrange
        // Act
        Optional<Room> getExit = room.getExit("invalid");
        // Assert
        assertTrue(getExit.isEmpty());
    }

    @Test
    void getExit_EmptyString_EmptyOptional() {
        // Arrange
        // Act
        Optional<Room> getExit = room.getExit("");
        // Assert
        assertTrue(getExit.isEmpty());
    }

    @Test
    void getExit_NullName_EmptyOptional() {
        // Arrange
        // Act
        Optional<Room> getExit = room.getExit(null);
        // Assert
        assertTrue(getExit.isEmpty());
    }

    @Test
    void getCharacter_ValidName_Character() {
        // Arrange
        Character addedCharacter = new Character("name");
        room.addCharacter(addedCharacter);
        // Act
        Optional<Character> gotCharacter = room.getCharacter(addedCharacter.getName());
        // Assert
        assertTrue(gotCharacter.isPresent());
        assertEquals(addedCharacter, gotCharacter.get());
    }

    @Test
    void getCharacter_DifferentCase_Item() {
        // Arrange
        Character addedCharacter = new Character("name");
        room.addCharacter(addedCharacter);
        // Act
        Optional<Character> gotCharacter = room.getCharacter("nAmE");
        // Assert
        assertTrue(gotCharacter.isPresent());
        assertEquals(addedCharacter, gotCharacter.get());
    }

    @Test
    void getCharacter_EmptyName_EmptyOptional() {
        // Arrange
        // Act
        Optional<Character> gotCharacter = room.getCharacter("");
        // Assert
        assertTrue(gotCharacter.isEmpty());
    }

    @Test
    void getCharacter_NullName_EmptyOptiona() {
        // Arrange
        // Act
        Optional<Character> gotCharacter = room.getCharacter(null);
        // Assert
        assertTrue(gotCharacter.isEmpty());
    }

    @Test
    void setExit_ValidExit_True() {
        // Arrange
        setExit = new Room("differentExit", List.of("e", "f"), "description", "hint", false);
        // Act
        boolean returnValue = room.setExit(setExit.getName(), setExit);
        // Assert
        assertTrue(returnValue);
    }

    @Test
    void setExit_NullName_False() {
        // Arrange
        setExit = new Room("differentExit", List.of("e", "f"), "description", "hint", false);
        // Act
        boolean returnValue = room.setExit(null, setExit);
        // Assert
        assertFalse(returnValue);
    }

    @Test
    void setExit_NullRoom_False() {
        // Arrange
        setExit = new Room("differentExit", List.of("e", "f"), "description", "hint", false);
        // Act
        boolean returnValue = room.setExit(setExit.getName(), null);
        // Assert
        assertFalse(returnValue);
    }

    @Test
    void addItem_ValidItem_True() {
        // Arrange
        Item item = new Item("name", "description", true, null);
        // Act
        boolean returnValue = room.addItem(item);
        // Assert
        assertTrue(returnValue);
    }

    @Test
    void addItem_NullItem_False() {
        // Arrange
        // Act
        boolean returnValue = room.addItem(null);
        // Assert
        assertFalse(returnValue);
    }

    @Test
    void addCharacter_ValidCharacter_True() {
        // Arrange
        Character character = new Character("name");
        // Act
        boolean returnValue = room.addCharacter(character);
        // Assert
        assertTrue(returnValue);
    }

    @Test
    void addCharacter_NullCharacter_False() {
        // Arrange
        // Act
        boolean returnValue = room.addCharacter(null);
        // Assert
        assertFalse(returnValue);
    }

    @Test
    void removeItem_NullName_EmptyOptional() {
        // Arrange
        // Act
        Optional<Item> item = room.removeItem(null);
        // Assert
        assertTrue(item.isEmpty());
    }

    @Test
    void removeItem_NotInRoom_EmptyOptional() {
        // Arrange
        // Act
        Optional<Item> item = room.removeItem("item");
        // Assert
        assertTrue(item.isEmpty());
    }

    @Test
    void removeItem_ValidItem_Item() {
        // Arrange
        Item addedItem = new Item("name", "description", true, null);
        room.addItem(addedItem);
        // Act
        Optional<Item> removedItem = room.removeItem(addedItem.getName());
        // Assert
        assertTrue(removedItem.isPresent());
        assertEquals(addedItem, removedItem.get());
    }

    @Test
    void removeItem_DifferentCase_Item() {
        // Arrange
        Item addedItem = new Item("NAme", "description", true, null);
        room.addItem(addedItem);
        // Act
        Optional<Item> removedItem = room.removeItem("naME");
        // Assert
        assertTrue(removedItem.isPresent());
        assertEquals(addedItem, removedItem.get());
    }

    @Test
    void removeItem_EmptyName_EmptyOptional() {
        // Arrange
        // Act
        Optional<Item> item = room.removeItem("");
        // Assert
        assertTrue(item.isEmpty());
    }

    @Test
    void unlock_RoomLocked_RoomUnlocked() {
        // Arrange
        room.setLocked(true);
        // Act
        room.unlock();
        // Assert
        assertFalse(room.isLocked());
    }

    @Test
    void unlock_RoomUnlocked_RoomUnlocked() {
        // Arrange
        room.setLocked(false);
        // Act
        room.unlock();
        // Assert
        assertFalse(room.isLocked());
    }
}