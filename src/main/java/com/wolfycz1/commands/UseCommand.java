package com.wolfycz1.commands;

import com.wolfycz1.Command;

public class UseCommand implements Command {
    @Override
    public String execute(String argument) {
        return null;
    }

    @Override
    public String getDescription() {
        return "Uses an item. [u]";
    }

    @Override
    public String getDetails() {
        return """
               USE item
                    item - item from your inventory to be used.
               
               Example:
                    use Rusty Key""";
    }

    @Override
    public boolean exit() {
        return false;
    }
}
