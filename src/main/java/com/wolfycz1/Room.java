package com.wolfycz1;

import java.util.List;
import java.util.Map;

public class Room {
    private String name;
    private Map<String, Room> exits;
    private List<Item> items;
    private List<Character> characters;
    private boolean isLocked;

    public Room getExit(String roomName) {
        return null;
    }

    public Room setExit(String name, Room room) {
        return null;
    }

    public void addItem(Item item) {}

    public void removeItem(Item item) {}

    public Item getItem(String itemName) {
        return null;
    }

    public boolean hasItem(String itemName) {
        return false;
    }

    public void addCharacter(Character character) {}

    public Character getCharacter(String characterName) {
        return null;
    }

    public void unlock() {}
}
