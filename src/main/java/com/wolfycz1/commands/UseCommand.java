package com.wolfycz1.commands;

import com.wolfycz1.*;
import com.wolfycz1.models.Item;
import com.wolfycz1.models.Item.UsageEffect;
import com.wolfycz1.models.Room;
import com.wolfycz1.utils.Language;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles the player action of applying an item from their inventory to the game world.
 * @author woflycz1
 */
@AllArgsConstructor
public class UseCommand implements Command {
    private final Console console;

    /**
     * Executes the use sequence. If the item is a key, it attempts to unlock an adjacent room.
     * If the item has a specific {@link UsageEffect}, it attempts to trigger that effect.
     * @param argument The name of the item the player wants to use.
     * @return A {@code CommandResponse} with a reponse and exit status.
     */
    @Override
    public CommandResponse execute(String argument) {
        if (argument.isEmpty()) return new CommandResponse(String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.use"))), false);

        Optional<Item> optItem = console.getInventory().getItem(argument);
        if (optItem.isEmpty()) return new CommandResponse(Language.get("cmd.use.err.noItem"), false);
        Item item = optItem.get();

        Room currentRoom = console.getCurrentRoom();

        if (item.getUnlocksRoom() != null) {
            Room targetRoom = item.getUnlocksRoom();
            Optional<Room> optRoom = currentRoom.getExit(targetRoom.getName());
            if (optRoom.isPresent()) {
                if (!targetRoom.isLocked()) {
                    return new CommandResponse(Language.get("cmd.use.err.unlocked"), false);
                }
                targetRoom.unlock();
                return new CommandResponse(Language.get("cmd.use.execute.key", targetRoom.getName()), false);
            }
            return new CommandResponse(Language.get("cmd.use.err.key", item.getName()), false);
        }

        if (item.getUsageEffect() != null) {
            return new CommandResponse(switch (item.getUsageEffect()) {
                case UsageEffect.RESTORE_POWER -> {
                    if (currentRoom.getName().equalsIgnoreCase(Language.get("room.generatorRoom"))) {
                        console.setWinState(true);
                        currentRoom.setDescription(Language.get("cmd.use.restorePower.newDesc"));
                        yield Language.get("cmd.use.execute.restorePower", item.getName());
                    }
                    yield Language.get("cmd.use.err.restorePower");
                }
            }, false);
        }

        return new CommandResponse(Language.get("cmd.use.err.noUse"), false);
    }

    /**
     * Retrieves a summary of the use command with aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.use.desc") + " " + Arrays.toString(Language.getArray("cmd.use.aliases"));
    }

    /**
     * Retrieves the manual entry for the use command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
               
               %s
                    %s""", Language.get("man.use.cmd"), Language.get("man.use.arg"), Language.get("man.example"),
                Language.get("man.use.example"));
    }
}
