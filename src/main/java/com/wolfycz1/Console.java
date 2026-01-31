package com.wolfycz1;

import com.wolfycz1.commands.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.util.*;

@Slf4j
@Getter
@Setter
public class Console {
    private final Map<String, Command> commands;
    private final List<String> commandList;
    private DialogueHandler dialogueHandler;
    private boolean exit;
    private boolean dialogueActive;
    private Room currentRoom;
    private final Inventory inventory;
    private Terminal terminal;
    private LineReader reader;
    private boolean winState;
    private Room winRoom;

    public Console() {
        commands = new HashMap<>();
        commandList = new ArrayList<>();
        inventory = new Inventory();
        dialogueHandler = new DialogueHandler();
    }

    public void initialize() {
        try {
            terminal = TerminalBuilder.builder().system(true).jansi(true).build();
        } catch (IOException e) {
            log.warn("IO Exception triggered while building terminal.");
        }

        reader = LineReaderBuilder.builder().terminal(terminal).build();
        JLineAppender.setLineReader(reader);

        setLanguage();

        WorldLoader worldLoader = new WorldLoader();
        Room[] loadedRooms = worldLoader.load(Language.get("data.json"));
        currentRoom = loadedRooms[0];
        if (currentRoom == null) {
            close();
            return;
        }
        winRoom = loadedRooms[1];

        inventory.addItem(currentRoom.getExit("Cinema Lobby").getExit("Food Court").getItem("Old Pizza Slice"));
        inventory.addItem(currentRoom.getExit("Electronics Store").getItem("Fusion Battery"));

        register(Language.get("cmd.go"), new GoCommand(this), Language.getArray("cmd.go.aliases"));
        register(Language.get("cmd.help"), new HelpCommand(this), Language.getArray("cmd.help.aliases"));
        register(Language.get("cmd.hint"), new HintCommand(this), Language.getArray("cmd.hint.aliases"));
        register(Language.get("cmd.interact"), new InteractCommand(this), Language.getArray("cmd.interact.aliases"));
        register(Language.get("cmd.pickup"), new PickupCommand(this), Language.getArray("cmd.pickup.aliases"));
        register(Language.get("cmd.drop"), new DropCommand(this), Language.getArray("cmd.drop.aliases"));
        register(Language.get("cmd.investigate"), new InvestigateCommand(this), Language.getArray("cmd.investigate.aliases"));
        register(Language.get("cmd.use"), new UseCommand(this), Language.getArray("cmd.use.aliases"));
        register(Language.get("cmd.exit"), new ExitCommand(), Language.getArray("cmd.exit.aliases"));

        terminal.writer().println(commands.get(Language.get("cmd.investigate")).execute("INTERNAL"));
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

            if (winState && (winRoom == null || winRoom == currentRoom)) {
                terminal.writer().println(Language.get("win.message"));
                close();
                return;
            }
        }
        close();
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
        try {
            String in = reader.readLine(">> ");
            if (in.isEmpty()) return;
            in = in.trim().toLowerCase();

            if (dialogueActive) {
                String response = dialogueHandler.processInput(in);
                if (response == null) {
                    dialogueActive = false;
                    terminal.writer().println(">> " + Language.get("console.info.convEnd"));
                } else {
                    terminal.writer().printf(">> %s%n", response);
                }
                return;
            }

            String[] parsedInput = parse(in);
            String command = parsedInput[0];
            String argument = parsedInput[1];
            log.info("Command: \"{}\" Argument: \"{}\"", command, argument);

            if (commands.containsKey(command)) {
                terminal.writer().printf(">> %s%n", commands.get(command).execute(argument));
                exit = commands.get(command).exit();
            } else {
                terminal.writer().println(">> " + Language.get("console.err.notRecognized", command, Language.get("cmd.help")));
            }
            terminal.writer().println();
        } catch (NoSuchElementException e) {
            log.warn("No such element exception triggered at scanner.");
        } catch (UserInterruptException e) {
            log.warn("User interrupt Exception triggered at Terminal.writer()");
        } catch (EndOfFileException e) {
            log.warn("End of File Exception triggered at Line Reader.");
        }
    }

    private String[] parse(String input) {
        String[] inputParts = input.trim().split("\\s+", 2);

        String command = inputParts[0];
        String argument = (inputParts.length > 1) ? inputParts[1] : "";

        return new String[]{command, argument};
    }

    private void setLanguage() {
        terminal.writer().println("[DEFAULT] English");
        terminal.writer().println("[2] česky");
        terminal.writer().println("[3] polski");
        try {
            String input = reader.readLine(">> ").trim();

            Locale locale = switch (input) {
                case "2" -> Locale.forLanguageTag("cs-CZ");
                case "3" -> Locale.forLanguageTag("pl-PL");
                default -> Locale.ENGLISH;
            };

            Language.load(locale);
        } catch (UserInterruptException e) {
            log.warn("User interrupt exception triggered at Terminal.reader()");
        }

        terminal.writer().println(Language.get("console.info.langSelect"));
    }

    private void close() {
        try {
            terminal.close();
        } catch (IOException e) {
            log.warn("IO Exception while closing Terminal");
        }
    }
}