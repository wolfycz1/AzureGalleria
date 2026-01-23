package com.wolfycz1;

public interface Command {
    String execute(String argument);
    String getDescription();
    String getDetails();
    boolean exit();
}
