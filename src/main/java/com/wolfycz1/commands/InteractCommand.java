package com.wolfycz1.commands;

import com.wolfycz1.*;
import com.wolfycz1.Character;
import lombok.AllArgsConstructor;
import com.wolfycz1.Character.Trade;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@AllArgsConstructor
public class InteractCommand implements Command {
    private final Console console;

    @Override
    public String execute(String argument) {
        if (argument.isEmpty()) return String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.interact")));

        Character character = console.getCurrentRoom().getCharacter(argument);
        if (character == null) return Language.get("cmd.interact.err.noChar", argument);

        log.info("Character {} has trades: {}", character.getName(), character.getTrades() != null);
        if (character.getTrades() != null) {
            for (Trade trade : character.getTrades().values()) {
                String tradeIn = trade.getTradeIn();

                if (console.getInventory().hasItem(tradeIn)) {
                    Item item = console.getInventory().getItem(tradeIn);
                    console.getInventory().removeItem(item);

                    if (trade.getTradeOut() != null) {
                        console.getInventory().addItem(trade.getTradeOut());
                    }

                    console.setDialogueActive(true);

                    return console.getDialogueHandler().startDialogue(character, trade.getTradeDialogue());
                }
            }
        }

        if (character.getStartNode() == null) return Language.get("cmd.interact.err.noDialogue", character.getName());

        console.setDialogueActive(true);
        return console.getDialogueHandler().startDialogue(character);
    }

    @Override
    public String getDescription() {
        return Language.get("cmd.interact.desc") + " " + Arrays.toString(Language.getArray("cmd.interact.aliases"));
    }

    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
               
               %s
                    %s""", Language.get("man.interact.cmd"), Language.get("man.interact.arg"),
                Language.get("man.example"), Language.get("man.interact.example"));
    }

    @Override
    public boolean exit() {
        return false;
    }
}
