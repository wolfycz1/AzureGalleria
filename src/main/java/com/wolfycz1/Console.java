package com.wolfycz1;

import com.wolfycz1.commands.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
@Getter
@Setter
public class Console {
    private final Map<String, Command> commands;
    private final List<String> commandList;
    private DialogueHandler dialogueHandler;
    private final Scanner sc;
    private boolean exit;
    private Room currentRoom;
    private final Inventory inventory;

    public Console() {
        commands = new HashMap<>();
        commandList = new ArrayList<>();
        sc = new Scanner(System.in);
        inventory = new Inventory();
    }

    public void initialize() {
        if (System.console() == null) {
            /*
                This is here to prevent running Maven inside an IDE.
                Running Maven inside an IDE flushes incorrectly,
                which bufferes the print() method behind scanner.nextLine()
                resulting in an output like this:
                --------------------------------------------------------------------------------
                Code:                                   Output:                 Expected output:
                System.out.print(">> ");                input                   >> input
                scanner.nextLine();                     >> >>
                --------------------------------------------------------------------------------
                Please download Maven here: https://maven.apache.org/download.cgi
             */
            log.error("This application won't work correctly inside an IDE. Please use the terminal.");
            return;
        }

        WorldLoader worldLoader = new WorldLoader();
        currentRoom = worldLoader.load("data.json");
        if (currentRoom == null) return;

        register("go", new GoCommand(this), "g");
        register("help", new HelpCommand(commandList), "?");
        register("hint", new HintCommand(), "h");
        register("interact", new InteractCommand(), "i");
        register("pickup", new PickupCommand(this), "p");
        register("drop", new DropCommand(this), "d");
        register("investigate", new InvestigateCommand(), "f");
        register("use", new UseCommand(), "u");
        register("exit", new ExitCommand(), "e");

        while (!exit) {
            execute();
            log.debug("""
                    STATE
                        Current Room: {} {} {}
                            Items: {}
                            Characters: {}
                            Exits: {}
                        Inventory: {}
                    """, currentRoom.getName(), currentRoom.getAliases(), currentRoom.isLocked() ? "LOCKED" : "UNLOCKED",
                    currentRoom.listItems(), currentRoom.listCharacters(), currentRoom.listExits(), inventory.listItems());
        }
        sc.close();
    }

    private void register(String name, Command command, String... aliases) {
        log.info("Registering command {} with aliases {}", name, aliases);
        commands.put(name, command);
        for (String alias : aliases) {
            commands.put(alias, command);
        }
        commandList.add(name);
    }

    private void execute() {
        System.out.print(">> ");
        try {
            String in = sc.nextLine();
            if (in.isEmpty()) return;
            in = in.trim().toLowerCase();

            String[] parsedInput = parse(in);
            String command = parsedInput[0];
            String argument = parsedInput[1];
            log.info("Command: \"{}\" Argument: \"{}\"", command, argument);

            if (commands.containsKey(command)) {
                System.out.println(">> " + commands.get(command).execute(argument));
                exit = commands.get(command).exit();
            } else {
                System.err.printf(">> Command '%s' not recognized. See 'help'.\n", command);
            }
            System.out.println();
        } catch (NoSuchElementException e) {
            log.warn("No such element exception triggered at scanner.");
        }
    }

    private String[] parse(String input) {
        String[] inputParts = input.trim().split("\\s+", 2);

        String command = inputParts[0];
        String argument = (inputParts.length > 1) ? inputParts[1] : "";

        return new String[]{command, argument};
    }
}