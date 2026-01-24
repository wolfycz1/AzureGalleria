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
    private boolean dialogueActive;
    private Room currentRoom;
    private final Inventory inventory;

    public Console() {
        commands = new HashMap<>();
        commandList = new ArrayList<>();
        sc = new Scanner(System.in);
        inventory = new Inventory();
        dialogueHandler = new DialogueHandler();
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
            log.error(Language.get("console.err.IDE"));
            return;
        }

        WorldLoader worldLoader = new WorldLoader();
        currentRoom = worldLoader.load(Language.get("data.json"));
        if (currentRoom == null) return;

        register(Language.get("cmd.go"), new GoCommand(this), Language.getArray("cmd.go.aliases"));
        register(Language.get("cmd.help"), new HelpCommand(this), Language.getArray("cmd.help.aliases"));
        register(Language.get("cmd.hint"), new HintCommand(), Language.getArray("cmd.hint.aliases"));
        register(Language.get("cmd.interact"), new InteractCommand(this), Language.getArray("cmd.interact.aliases"));
        register(Language.get("cmd.pickup"), new PickupCommand(this), Language.getArray("cmd.pickup.aliases"));
        register(Language.get("cmd.drop"), new DropCommand(this), Language.getArray("cmd.drop.aliases"));
        register(Language.get("cmd.investigate"), new InvestigateCommand(), Language.getArray("cmd.investigate.aliases"));
        register(Language.get("cmd.use"), new UseCommand(), Language.getArray("cmd.use.aliases"));
        register(Language.get("cmd.exit"), new ExitCommand(), Language.getArray("cmd.exit.aliases"));

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

            if (dialogueActive) {
                String response = dialogueHandler.processInput(in);
                if (response == null) {
                    dialogueActive = false;
                    System.out.println(Language.get("console.info.convEnd"));
                } else {
                    System.out.printf(">> %s%n", response);
                }
                return;
            }

            String[] parsedInput = parse(in);
            String command = parsedInput[0];
            String argument = parsedInput[1];
            log.info("Command: \"{}\" Argument: \"{}\"", command, argument);

            if (commands.containsKey(command)) {
                System.out.printf(">> %s%n", commands.get(command).execute(argument));
                exit = commands.get(command).exit();
            } else {
                System.err.println(Language.get("console.err.notRecognized", command, "cmd.help"));
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