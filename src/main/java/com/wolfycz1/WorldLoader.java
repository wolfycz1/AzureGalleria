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

@Slf4j
public class WorldLoader {
    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, Character> characters = new HashMap<>();
    private final Map<String, Item> items = new HashMap<>();
    private final Map<String, DialogueNode> dialogueNodes = new HashMap<>();

    public Room load(String filePath) {
        rooms.clear(); characters.clear(); items.clear();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath)){
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
            return rooms.get(worldMap.getStartingRoom());
        } catch (IOException e) {
            log.error("IO Exception when reading {}", filePath);
            return null;
        }
    }

    private void createDTOs(WorldMap worldMap) {
        for (CharacterDTO dto : worldMap.getCharacters()) {
            Character character = new Character(dto.getName());
            characters.put(dto.getName(), character);
        }

        for (ItemDTO dto : worldMap.getItems()) {
            Item item = new Item(dto.getName(), dto.getDescription(), dto.isPickupable());
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
                    dto.isLocked()
            );
            rooms.put(dto.getName(), room);
        }
        log.info("DTOs created.");
    }

    private void assignDTOs(WorldMap worldMap) {
        for (RoomDTO dto : worldMap.getRooms()) {
            Room room = rooms.get(dto.getName());

            for (String exitName : dto.getExits()) {
                Room exit = rooms.get(exitName);

                if (exit != null) {
                    room.setExit(exitName, exit);
                } else {
                    log.warn("Room {} has no exits.", exitName);
                }
            }

            for (String characterName : dto.getCharacters()) {
                Character character = characters.get(characterName);

                if (character != null) {
                    room.addCharacter(character);
                }
            }

            for (String itemName : dto.getItems()) {
                Item item = items.get(itemName);

                if (item != null) {
                    room.addItem(item);
                }
            }
        }

        for (DialogueNodeDTO nodeDTO : worldMap.getDialogues()) {
            DialogueNode sourceNode = dialogueNodes.get(nodeDTO.getId());

            if (nodeDTO.getOptions() != null) {
                for (DialogueOptionDTO optionDTO : nodeDTO.getOptions()) {
                    DialogueNode targetNode = dialogueNodes.get(optionDTO.getNextNode());

                    sourceNode.addOption(new DialogueOption(optionDTO.getLabel(), targetNode));
                }
            }
        }

        for (CharacterDTO characterDTO : worldMap.getCharacters()) {
            Character character = characters.get(characterDTO.getName());

            if (characterDTO.getStartNode() != null) {
                DialogueNode startNode = dialogueNodes.get(characterDTO.getStartNode());
                character.setStartNode(startNode);
            }
        }
        log.info("DTOs assigned.");
    }

    @Data
    private static class WorldMap {
        private List<RoomDTO> rooms;
        private List<CharacterDTO> characters;
        private List<ItemDTO> items;
        private List<DialogueNodeDTO> dialogues;
        private String startingRoom;
    }

    @Data
    private static class RoomDTO {
        private String name;
        private List<String> aliases;
        private String description;
        private List<String> exits;
        private List<String> characters;
        private List<String> items;
        private boolean locked;
    }

    @Data
    private static class CharacterDTO {
        private String name;
        private String startNode;
    }

    @Data
    private static class ItemDTO {
        private String name;
        private String description;
        private boolean pickupable;
    }

    @Data
    private static class DialogueNodeDTO {
        private String id;
        private String text;
        private List<DialogueOptionDTO> options;
    }

    @Data
    private static class DialogueOptionDTO {
        private String label;
        private String nextNode;
    }
}
