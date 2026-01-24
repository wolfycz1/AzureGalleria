package com.wolfycz1.commands;

import com.wolfycz1.Command;
import com.wolfycz1.Language;

import java.util.Arrays;

public class UseCommand implements Command {
    @Override
    public String execute(String argument) {
        return null;
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
