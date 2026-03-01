package com.wolfycz1.commands;

/**
 * Defines the contract for all executable actions within the game.
 * @author wolfycz1
 */
public interface Command {
    String execute(String argument);
    String getDescription();
    String getDetails();
    boolean exit();
}
