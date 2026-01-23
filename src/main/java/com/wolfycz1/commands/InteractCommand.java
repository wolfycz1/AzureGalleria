package com.wolfycz1.commands;

import com.wolfycz1.Character;
import com.wolfycz1.Command;
import com.wolfycz1.Console;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InteractCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return "No argument specified. See 'help interact'.";

        Character character = console.getCurrentRoom().getCharacter(argument);
        if (character == null) return String.format("There is no one named '%s' in this room.", argument);

        if (character.getStartNode() == null) return String.format("%s doesn't seem to want to talk to you at the moment.", character.getName());

        console.setDialogueActive(true);
        return console.getDialogueHandler().startDialogue(character);
    }

    @Override
    public String getDescription() {
        return "Talks to a character. [i]";
    }

    @Override
    public String getDetails() {
        return """
               INTERACT character
                    character - character in the current room to initiate a conversation with.
               
               Example:
                    interact The Angler""";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
