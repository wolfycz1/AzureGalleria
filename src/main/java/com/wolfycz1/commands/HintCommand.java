package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Language;

import java.util.Arrays;

public class HintCommand implements Command {
    @Override
    public String execute(String argument) {
        return null;
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
