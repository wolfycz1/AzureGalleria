package com.wolfycz1;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Room {
    private String name;
    private List<String> aliases;
    private String description;
    private Map<String, Room> exits;
    private List<Character> characters;
    private List<Item> items;
    private boolean isLocked;

    public Room(String name, List<String> aliases, String description, boolean isLocked) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.exits = new HashMap<>();
        this.characters = new ArrayList<>();
        this.items = new ArrayList<>();
        this.isLocked = isLocked;
    }

    public Room getExit(String roomName) {
        return null;
    }

    public void setExit(String name, Room room) {
        exits.put(name, room);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {}

    public Item getItem(String itemName) {
        return null;
    }

    public boolean hasItem(String itemName) {
        return false;
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public Character getCharacter(String characterName) {
        return null;
    }

    public void unlock() {}
}
