package com.wolfycz1.commands;

import com.wolfycz1.Character;
import com.wolfycz1.Command;
import com.wolfycz1.Console;
import com.wolfycz1.Language;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class InteractCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.interact")));

        Character character = console.getCurrentRoom().getCharacter(argument);
        if (character == null) return Language.get("cmd.interact.err.noChar", argument);

        if (character.getStartNode() == null) return Language.get("cmd.interact.err.noDialogue", character.getName());

        console.setDialogueActive(true);
        return console.getDialogueHandler().startDialogue(character);
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.interact.desc") + " " + Arrays.toString(Language.getArray("cmd.interact.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
               
               %s
                    %s""", Language.get("man.interact.cmd"), Language.get("man.interact.arg"),
                Language.get("man.example"), Language.get("man.interact.example"));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
