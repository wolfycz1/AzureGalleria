package com.wolfycz1.commands;

/**
 * Defines the contract for all executable actions within the game.
 * @author wolfycz1
 */
public interface Command {
    CommandResponse execute(String argument);
    String getDescription();
    String getDetails();
    record CommandResponse(String response, boolean shouldExit) {}
}
