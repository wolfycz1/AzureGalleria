package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Language;

import java.util.Arrays;

public class InvestigateCommand implements Command {
    @Override
    public String execute(String argument) {
        return null;
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.investigate.desc") + " " + Arrays.toString(Language.getArray("cmd.investigate.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
               %s""", Language.get("man.investigate.cmd"), Language.get("man.investigate.desc"));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
