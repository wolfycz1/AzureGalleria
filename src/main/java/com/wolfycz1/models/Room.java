package com.wolfycz1.models;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Represents a location within the game world.
 * A room acts as a container for characters and items, and connects to other rooms via exits.
 * It also manages its own locked state and can be identified by its primary name or aliases.
 * @author wolfycz1
 */
@Getter
@Setter
public class Room {
    private final String name;
    private final List<String> aliases;
    private String description;
    private final String hint;
    private final Map<String, Room> exits;
    private final List<Character> characters;
    private final List<Item> items;
    private boolean locked;

    public Room(String name, List<String> aliases, String description, String hint, boolean locked) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.hint = hint;
        this.exits = new HashMap<>();
        this.characters = new ArrayList<>();
        this.items = new ArrayList<>();
        this.locked = locked;
    }

    /**
     * Searches for a connected exit by the destination room's primary name or any of its aliases. (case-insensitive)
     * @param roomName The name or alias of the desired destination room.
     * @return An {@link Optional} containing the Room if a match is found, otherwise empty.
     */
    public Optional<Room> getExit(String roomName) {
        for (Room room : exits.values()) {
            if (room.getName().equalsIgnoreCase(roomName)) return Optional.of(room);
            for (String alias : room.getAliases()) {
                if (alias.equalsIgnoreCase(roomName)) return Optional.of(room);
            }
        }
        return Optional.empty();
    }

    /**
     * Searches for an item within the current room. (case-insensitive)
     * @param itemName Name of the item to find.
     * @return An {@link Optional} containing the Item if found, otherwise empty.
     */
    private Optional<Item> getItem(String itemName) {
        if (itemName == null) return Optional.empty();
        return items.stream().filter(i -> i.getName().equalsIgnoreCase(itemName)).findFirst();
    }

    /**
     * Searches for a character within the current room. (case-insensitive)
     * @param characterName Name of the character to find.
     * @return An {@link Optional} containing the Character if found, otherwise empty.
     */
    public Optional<Character> getCharacter(String characterName) {
        if (characterName == null) return Optional.empty();
        return characters.stream().filter(c -> c.getName().equalsIgnoreCase(characterName)).findFirst();
    }

    /**
     * Creates a one-way connection from this room to another room.
     * @param name Name of the room.
     * @param room The destination Room object.
     * @return {@code true} if the exit was successfully added, {@code false} if either parameter was null.
     */
    public boolean setExit(String name, Room room) {
        if (name == null || room == null) return false;
        exits.put(name, room);
        return true;
    }

    /**
     * Adds an item into the room.
     * @param item The Item to add.
     * @return {@code true} if the item was successfully added, {@code false} if the item was null.
     */
    public boolean addItem(Item item) {
        if (item == null) return false;
        items.add(item);
        return true;
    }

    /**
     * Adds a character into the room.
     * @param character The Character to add.
     * @return {@code true} if the character was successfully added, {@code false} if the character was null.v
     */
    public boolean addCharacter(Character character) {
        if (character == null) return false;
        characters.add(character);
        return true;
    }

    /**
     * Attempts to find and remove an item from the room based on its name.
     * @param itemName Name of the item to remove.
     * @return An {@link Optional} containing the removed Item if successful, or empty if the item wasn't found.
     */
    public Optional<Item> removeItem(String itemName) {
        Optional<Item> item = getItem(itemName);
        item.ifPresent(items::remove);
        return item;
    }

    /**
     * Returns a string representation of the exits in the current room.
     * @return A formatted string of exit names or an empty string if there are no exits.
     */
    public String listExits() {
        if (exits.isEmpty()) return "";
        return exits.keySet().toString();
    }

    /**
     * Returns a string representation of the items in the current room.
     * @return A formatted string of item names or an empty string if there are no items.
     */
    public String listItems() {
        if (items.isEmpty()) return "";
        return items.stream().map(Item::getName).toList().toString();
    }

    /**
     * Returns a string representation of the characters in the current room.
     * @return A formatted string of character names or an empty string if there are no characters.
     */
    public String listCharacters() {
        if (characters.isEmpty()) return "";
        return characters.stream().map(Character::getName).toList().toString();
    }

    /**
     * Unlocks the room.
     */
    public void unlock() {
        this.locked = false;
    }
}
