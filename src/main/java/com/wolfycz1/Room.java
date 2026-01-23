package com.wolfycz1;

import lombok.Getter;

import java.util.*;

@Getter
public class Room {
    private String name;
    private List<String> aliases;
    private String description;
    private Map<String, Room> exits;
    private List<Character> characters;
    private List<Item> items;
    private boolean locked;

    public Room(String name, List<String> aliases, String description, boolean locked) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.exits = new HashMap<>();
        this.characters = new ArrayList<>();
        this.items = new ArrayList<>();
        this.locked = locked;
    }

    public Room getExit(String roomName) {
        for (Room room : exits.values()) {
            if (room.getName().equalsIgnoreCase(roomName)) return room;
            for (String alias : room.getAliases()) {
                if (alias.equalsIgnoreCase(roomName)) return room;
            }
        }
        return null;
    }

    public void setExit(String name, Room room) {
        exits.put(name, room);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item getItem(String itemName) {
        return items.stream().filter(i -> Objects.equals(i.getName().toLowerCase(), itemName.toLowerCase())).findFirst().orElse(null);
    }

    public boolean hasItem(String itemName) {
        return items.stream().anyMatch(i -> Objects.equals(i.getName().toLowerCase(), itemName.toLowerCase()));
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public Character getCharacter(String characterName) {
        return characters.stream().filter(c -> Objects.equals(c.getName().toLowerCase(), characterName.toLowerCase())).findFirst().orElse(null);
    }

    public String listExits() {
        return exits.keySet().toString();
    }

    public String listItems() {
        return items.stream().map(Item::getName).toList().toString();
    }

    public String listCharacters() {
        return characters.stream().map(Character::getName).toList().toString();
    }

    public void unlock() {}
}
