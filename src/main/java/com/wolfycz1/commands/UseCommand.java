package com.wolfycz1.commands;

import com.wolfycz1.*;
import com.wolfycz1.Item.UsageEffect;
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
     * @return A localized status string describing the outcome of the interaction,
     * or an errorif the item cannot be used in the current context.
     */
    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.use")));

        Optional<Item> optItem = console.getInventory().getItem(argument);
        if (optItem.isEmpty()) return Language.get("cmd.use.err.noItem");
        Item item = optItem.get();

        Room currentRoom = console.getCurrentRoom();

        if (item.getUnlocksRoom() != null) {
            Room targetRoom = item.getUnlocksRoom();
            Optional<Room> optRoom = currentRoom.getExit(targetRoom.getName());
            if (optRoom.isPresent()) {
                if (!targetRoom.isLocked()) {
                    return Language.get("cmd.use.err.unlocked");
                }
                targetRoom.unlock();
                return Language.get("cmd.use.execute.key", targetRoom.getName());
            }
            return Language.get("cmd.use.err.key", item.getName());
        }

        if (item.getUsageEffect() != null) {
            //noinspection SwitchStatementWithTooFewBranches
            switch (item.getUsageEffect()) {
                case UsageEffect.RESTORE_POWER -> {
                    if (currentRoom.getName().equalsIgnoreCase(Language.get("room.generatorRoom"))) {
                        console.setWinState(true);
                        currentRoom.setDescription(Language.get("cmd.use.restorePower.newDesc"));
                        return Language.get("cmd.use.execute.restorePower", item.getName());
                    }
                    return Language.get("cmd.use.err.restorePower");
                }

                default -> {
                    return Language.get("cmd.use.err.noKnowledge");
                }
            }
        }

        return Language.get("cmd.use.err.noUse");
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

    /**
     * Indicates whether executing this command terminates the game.
     * @return always {@code false}
     */
    @Override
    public boolean exit() {
        return false;
    }
}
