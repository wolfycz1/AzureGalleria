package com.wolfycz1;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wolfycz1.Item.UsageEffect;

/**
 * Handles the loading of the game world from a JSON file.
 * Uses a two-pass loading system to instantiate domain objects and resolve circular dependencies.
 * @author wolfycz1
 */
@Slf4j
public class WorldLoader {
    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, Character> characters = new HashMap<>();
    private final Map<String, Item> items = new HashMap<>();
    private final Map<String, DialogueNode> dialogueNodes = new HashMap<>();

    /**
     * Reads and parses the world file to construct the game state.
     * @param filePath The relative path to the JSON world file.
     * @return A {@link LoadedWorld} record containing the starting room and the optional win room.
     * Returns null if the file cannot be read or is missing.
     */
    public LoadedWorld load(String filePath) {
        rooms.clear(); characters.clear(); items.clear(); dialogueNodes.clear();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);                            // IGNORES $SCHEMA
            if (inputStream == null) {
                log.error("Critical error: World file is missing! {}", filePath);
                return null;
            }
            WorldMap worldMap = mapper.readValue(inputStream, WorldMap.class);
            log.info("Parsed world file successfully.");

            createDTOs(worldMap);
            assignDTOs(worldMap);

            Room winRoom = null;
            if (worldMap.getWinRoom() != null) {
                winRoom = rooms.get(worldMap.getWinRoom());
            }

            if (rooms.get(worldMap.getStartingRoom()) == null) {
                return null;
            }

            return new LoadedWorld(rooms.get(worldMap.getStartingRoom()), winRoom);
        } catch (IOException e) {
            log.error("IO Exception when reading {}", filePath);
            return null;
        }
    }

    /**
     * A data carrier containing the anchor points of the loaded game world.
     * @param startingRoom The room where the player begins the game.
     * @param winRoom The room the player may reach to trigger the win condition.
     */
    public record LoadedWorld(Room startingRoom, Room winRoom) {}

    /**
     * Instantiates the core domain objects from their respective Data Transfer Objects and stores them in lookup maps.
     * Dependencies between objects are not resolved in this step.
     * @param worldMap The parsed JSON representation of the world.
     */
    private void createDTOs(WorldMap worldMap) {
        for (CharacterDTO dto : worldMap.getCharacters()) {
            Character character = new Character(dto.getName());
            characters.put(dto.getName(), character);
        }

        for (ItemDTO dto : worldMap.getItems()) {
            UsageEffect usageEffect = UsageEffect.fromString(dto.getUsageEffect());
            log.info("Item {} has the usage effect {}", dto.getName(), usageEffect);
            Item item = new Item(dto.getName(), dto.getDescription(), dto.isPickupable(), usageEffect);
            items.put(dto.getName(), item);
        }

        for (DialogueNodeDTO dto : worldMap.getDialogues()) {
            DialogueNode dialogueNode = new DialogueNode(dto.getId(), dto.getText());
            dialogueNodes.put(dto.getId(), dialogueNode);
        }

        for (RoomDTO dto : worldMap.getRooms()) {
            Room room = new Room(
                    dto.getName(),
                    dto.getAliases(),
                    dto.getDescription(),
                    dto.getHint(),
                    dto.isLocked()
            );
            rooms.put(dto.getName(), room);
        }
        log.info("DTOs created.");
    }

    /**
     * Iterates through the raw DTOs again to link the instantiated domain objects together.
     * @param worldMap The parsed JSON representation of the world.
     */
    private void assignDTOs(WorldMap worldMap) {
        for (RoomDTO dto : worldMap.getRooms()) {
            Room room = rooms.get(dto.getName());

            for (String exitName : dto.getExits()) {
                Room exit = rooms.get(exitName);

                if (!room.setExit(exitName, exit)) {
                    log.warn("Exit room {} does not exist.", exitName);
                }
            }

            for (String characterName : dto.getCharacters()) {
                Character character = characters.get(characterName);

                if (!room.addCharacter(character)) {
                    log.warn("Couldn't find character {}", characterName);
                }
            }

            for (String itemName : dto.getItems()) {
                Item item = items.get(itemName);

                if (!room.addItem(item)) {
                    log.warn("Couldn't find item {}", itemName);
                }
            }
        }

        for (DialogueNodeDTO nodeDTO : worldMap.getDialogues()) {
            DialogueNode sourceNode = dialogueNodes.get(nodeDTO.getId());

            if (nodeDTO.getOptions() != null) {
                for (DialogueOptionDTO optionDTO : nodeDTO.getOptions()) {
                    DialogueNode targetNode = dialogueNodes.get(optionDTO.getNextNode());

                    if (!sourceNode.addOption(new DialogueOption(optionDTO.getLabel(), targetNode))) {
                        log.warn("Dialogue Option wasn't added because the option is null");
                    }
                }
            }
        }

        for (CharacterDTO characterDTO : worldMap.getCharacters()) {
            Character character = characters.get(characterDTO.getName());

            if (characterDTO.getStartNode() != null) {
                DialogueNode startNode = dialogueNodes.get(characterDTO.getStartNode());
                character.setStartNode(startNode);
            }

            log.info("CharacterDTO {} has trades: {}", characterDTO.getName(), characterDTO.getTrades() != null);
            if (characterDTO.getTrades() != null) {
                for (TradeDTO dto : characterDTO.getTrades()) {
                    Item tradeOut = items.get(dto.getTradeOut());
                    DialogueNode tradeDialogue = dialogueNodes.get(dto.getTradeDialogue());

                    if (!character.addTrade(dto.getTradeIn(), tradeOut, tradeDialogue)) {
                        log.warn("Trade for {} is missing valid Item or Dialogue references.", character.getName());
                    }
                }
            }
        }

        for (ItemDTO itemDTO : worldMap.getItems()) {
            if (itemDTO.getUnlocksRoom() != null) {
                Item item = items.get(itemDTO.getName());
                Room room = rooms.get(itemDTO.getUnlocksRoom());

                if (item != null && room != null) {
                    item.setUnlocksRoom(room);
                    log.info("Item {} unlocks {}", item.getName(), room.getName());
                } else {
                    log.warn("Item {} unlocks a non-existent room {}", itemDTO.getName(), itemDTO.getUnlocksRoom());
                }
            }
        }
        log.info("DTOs assigned.");
    }

    /**
     * Root structure representing the world.
     */
    @Data
    private static class WorldMap {
        private List<RoomDTO> rooms;
        private List<CharacterDTO> characters;
        private List<ItemDTO> items;
        private List<DialogueNodeDTO> dialogues;
        private String startingRoom;
        private String winRoom;
    }

    /**
     * Data transfer object representing a raw room entry.
     */
    @Data
    private static class RoomDTO {
        private String name;
        private List<String> aliases;
        private String description;
        private String hint;
        private List<String> exits;
        private List<String> characters;
        private List<String> items;
        private boolean locked;
    }

    /**
     * Data transfer object representing a raw character entry.
     */
    @Data
    private static class CharacterDTO {
        private String name;
        private String startNode;
        private List<TradeDTO> trades;
    }

    /**
     * Data transfer object representing a raw trade entry.
     */
    @Data
    private static class TradeDTO {
        private String tradeIn;
        private String tradeOut;
        private String tradeDialogue;
    }

    /**
     * Data transfer object representing a raw item entry.
     */
    @Data
    private static class ItemDTO {
        private String name;
        private String description;
        private boolean pickupable;
        private String unlocksRoom;
        private String usageEffect;
    }

    /**
     * Data transfer object representing a raw dialogue node entry.
     */
    @Data
    private static class DialogueNodeDTO {
        private String id;
        private String text;
        private List<DialogueOptionDTO> options;
    }

    /**
     * Data transfer object representing a raw dialogue option entry.
     */
    @Data
    private static class DialogueOptionDTO {
        private String label;
        private String nextNode;
    }
}
