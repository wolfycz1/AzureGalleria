package com.wolfycz1.commands;

import com.wolfycz1.*;
import com.wolfycz1.Item.UsageEffect;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class UseCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.use")));

        Item item = console.getInventory().getItem(argument);
        if (item == null) return Language.get("cmd.use.err.noItem");

        Room currentRoom = console.getCurrentRoom();

        if (item.getUnlocksRoom() != null) {
            Room targetRoom = item.getUnlocksRoom();
            if (currentRoom.getExit(targetRoom.getName()) != null) {
                targetRoom.unlock();
                return Language.get("cmd.use.execute.key", targetRoom.getName());
            }
            return Language.get("cmd.use.err.key", item.getName());
        }

        if (item.getUsageEffect() != null) {
            switch (item.getUsageEffect()) {
                case UsageEffect.RESTORE_POWER -> {
                    if (currentRoom.getName().equalsIgnoreCase("Generator Room")) {
                        console.setWinState(true);
                        return Language.get("cmd.use.execute.restorePower");
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

    @Override
    public String getDescription() {
        return Language.get("cmd.use.desc") + " " + Arrays.toString(Language.getArray("cmd.use.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
               
               %s
                    %s""", Language.get("man.use.cmd"), Language.get("man.use.arg"), Language.get("man.example"),
                Language.get("man.use.example"));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
