package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Language;

import java.util.Arrays;

public class ExitCommand implements Command {

    @Override
    public String execute(String argument) {
        return Language.get("cmd.exit.execute");
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.exit.desc") + " " + Arrays.toString(Language.getArray("cmd.exit.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
               %s""", Language.get("man.exit.cmd"), Language.get("man.exit.desc"));
    }

    @Override
    public boolean exit() {
        return true;
    }
}
