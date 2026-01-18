package com.wolfycz1;

public interface Command {
    String execute(String argument);
    boolean exit();
}
