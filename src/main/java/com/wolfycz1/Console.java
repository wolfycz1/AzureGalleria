package com.wolfycz1;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Console {
    private Map<String, Command> commands;
    private List<String> commandList;
    private DialogueHandler dialogueHandler;
    private Scanner sc;
    private boolean exit;
    private Room currentRoom;
    private Inventory inventory;

    private void initialize() {}

    private void register(String name, Command command, String... aliases) {}

    private void execute() {}

    private String[] parse(String input) {
        return null;
    }
}