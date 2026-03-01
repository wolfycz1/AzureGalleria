package com.wolfycz1;

import com.wolfycz1.commands.*;
import com.wolfycz1.dialogue.DialogueHandler;
import com.wolfycz1.models.Inventory;
import com.wolfycz1.models.Room;
import com.wolfycz1.utils.JLineAppender;
import com.wolfycz1.utils.Language;
import com.wolfycz1.utils.WorldLoader;
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

/**
 * The core controller for the game. Manages the primary game loop, terminal IO via JLine3 and global game state.
 * @author wolfycz1
 */
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

    /**
     * Initializes the game environment and starts the main game loop.
     * Initializes the terminal, prompts for language selection, loads the world data and registers all playable commands.
     */
    public void initialize() {
        setupTerminal();
        setLanguage();
        setupWorld();
        registerCommands();

        terminal.writer().println(breakupStringToLines(Language.get("game.intro")) + "\nPress enter to start.");
        reader.readLine();

        terminal.writer().println(commands.get(Language.get("cmd.investigate")).execute("INTERNAL"));
        while (!exit) {
            execute();
            log.debug("STATE\n\tCurrent Room: {} {} {}\n\t\tItems: {}\n\t\tCharacters {}\n\t\tExits: {}\n\tInventory: {}",
                    currentRoom.getName(), currentRoom.getAliases(), currentRoom.isLocked() ? "LOCKED" : "UNLOCKED",
                    currentRoom.listItems(), currentRoom.listCharacters(), currentRoom.listExits(), inventory.listItems());

            if (winState && (winRoom == null || winRoom == currentRoom)) {
                terminal.writer().println(breakupStringToLines(Language.get("win.message")));
                close();
                return;
            }
        }
        close();
    }

    /**
     * Registers a command and its associated aliases into the game.
     * @param name The primary name of the command.
     * @param command The Command object implementation to execute.
     * @param aliases Alternate strings that trigger the same command.
     */
    private void register(String name, Command command, String... aliases) {
        log.info("Registering command {} with aliases {}", name, aliases);
        commands.put(name, command);
        for (String alias : aliases) {
            commands.put(alias, command);
        }
        commandList.add(name);
    }

    /**
     * Reads input from the player and routes it appropriately.
     * If a conversation is active, the input is sent to the DialogueHandler.
     */
    private void execute() {
        try {
            String in = reader.readLine(">> ");
            if (in.isEmpty()) return;
            in = in.trim().toLowerCase();

            if (dialogueActive) {
                handleDialogue(in);
                return;
            }

            ParsedCommand parsedInput = parse(in);
            String command = parsedInput.command();
            String argument = parsedInput.argument();
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

    /**
     * Splits raw user input into a command keyword and its subsequent argument.
     * @param input The raw string input.
     * @return A {@link ParsedCommand} record containing the separated command and argument.
     */
    private ParsedCommand parse(String input) {
        String[] inputParts = input.trim().split("\\s+", 2);

        String command = inputParts[0];
        String argument = (inputParts.length > 1) ? inputParts[1] : "";

        return new ParsedCommand(command, argument);
    }

    /**
     * A data carrier representing a player's raw input split into a command and an argument.
     * @param command The primary command keyword.
     * @param argument The argument of the command.
     */
    private record ParsedCommand(String command, String argument) {}

    /**
     * Initializes the JLine3 terminal and configures the custom logging appender.
     */
    private void setupTerminal() {
        try {
            terminal = TerminalBuilder.builder().system(true).jansi(true).build();
        } catch (IOException e) {
            log.warn("IO Exception triggered while building terminal.");
        }

        reader = LineReaderBuilder.builder().terminal(terminal).build();
        JLineAppender.setLineReader(reader);
    }

    /**
     * Prompts the player to select their preferred language and loads the corresponding ResourceBundle for localization.
     */
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

        terminal.writer().println(Language.get("console.info.langSelect") + "\n");
    }

    /**
     * Loads the game world from the JSON data file and sets the initial player state.
     */
    private void setupWorld() {
        WorldLoader worldLoader = new WorldLoader();
        WorldLoader.LoadedWorld loadedWorld = worldLoader.load(Language.get("data.json"));
        if (loadedWorld == null) {
            close();
            return;
        }
        currentRoom = loadedWorld.startingRoom();
        winRoom = loadedWorld.winRoom();
    }

    /**
     * Instantiates and registers all available commands.
     */
    private void registerCommands() {
        register(Language.get("cmd.go"), new GoCommand(this), Language.getArray("cmd.go.aliases"));
        register(Language.get("cmd.help"), new HelpCommand(this), Language.getArray("cmd.help.aliases"));
        register(Language.get("cmd.hint"), new HintCommand(this), Language.getArray("cmd.hint.aliases"));
        register(Language.get("cmd.interact"), new InteractCommand(this), Language.getArray("cmd.interact.aliases"));
        register(Language.get("cmd.pickup"), new PickupCommand(this), Language.getArray("cmd.pickup.aliases"));
        register(Language.get("cmd.drop"), new DropCommand(this), Language.getArray("cmd.drop.aliases"));
        register(Language.get("cmd.investigate"), new InvestigateCommand(this), Language.getArray("cmd.investigate.aliases"));
        register(Language.get("cmd.use"), new UseCommand(this), Language.getArray("cmd.use.aliases"));
        register(Language.get("cmd.exit"), new ExitCommand(), Language.getArray("cmd.exit.aliases"));
    }

    /**
     * Passes input to the DialogueHandler.
     * @param input The numeric string of the dialogue option.
     */
    private void handleDialogue(String input) {
        DialogueHandler.DialogueResult response = dialogueHandler.processInput(input);
        switch (response.status()) {
            case ENDED -> {
                dialogueActive = false;
                terminal.writer().println(">> " + Language.get("console.info.convEnd"));
            }
            case ERROR -> terminal.writer().println(response.output());
            case CONTINUE -> terminal.writer().printf(">> %s%n", response.output());
        }
    }

    /**
     * Word-wraps a string of text to fit within a 100-character width.
     * @param text The raw string to be formatted.
     * @return The formatted string containing line breaks.
     */
    public String breakupStringToLines(String text) {
        StringBuilder line = new StringBuilder();
        StringBuilder formatted = new StringBuilder();
        for (String word : text.split(" ")) {
            if (word.contains("\n")) {
                formatted.append(line);
                line.setLength(0);
            }
            if ((line.length() + word.length()) > 100) {
                formatted.append(line).append("\n");
                line.setLength(0);
            }
            line.append(word).append(" ");
        }
        formatted.append(line).append("\n");
        return formatted.toString();
    }

    /**
     * Safely shuts down the terminal connection before exiting the program.
     */
    private void close() {
        try {
            terminal.close();
        } catch (IOException e) {
            log.warn("IO Exception while closing Terminal");
        }
    }
}