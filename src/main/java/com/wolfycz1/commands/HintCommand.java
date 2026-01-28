package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Console;
import com.wolfycz1.Language;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class HintCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        return console.getCurrentRoom().getHint();
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.hint.desc") + " " + Arrays.toString(Language.getArray("cmd.hint.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
               %s""", Language.get("man.hint.cmd"), Language.get("man.hint.desc"));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
