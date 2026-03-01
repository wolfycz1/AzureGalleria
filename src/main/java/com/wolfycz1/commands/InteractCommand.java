package com.wolfycz1.commands;

import com.wolfycz1.*;
import com.wolfycz1.models.Character;
import com.wolfycz1.dialogue.DialogueHandler;
import com.wolfycz1.models.Inventory;
import com.wolfycz1.models.Item;
import com.wolfycz1.utils.Language;
import lombok.AllArgsConstructor;
import com.wolfycz1.models.Character.Trade;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles player interactions with NPCs. This command evaluates available trades.
 * @author woflycz1
 */
@Slf4j
@AllArgsConstructor
public class InteractCommand implements Command {
    private final Console console;

    /**
     * Executes the interaction sequence. Checks if the player has an item that satisfies any of the character's trades.
     * If a trade is met, it swaps the items and triggers the trade dialogue. If no trades are met,
     * it falls back to the character's default dialogue.
     * @param argument The name of the character the player wants to speak to.
     * @return A {@code CommandResponse} with a reponse and exit status. Appends {@code Investigate} on success.
     */
    @Override
    public CommandResponse execute(String argument) {
        if (argument.isEmpty()) return new CommandResponse(String.format("%s %s", Language.get("cmd.err.noArg"),
                Language.get("cmd.seeCmd", Language.get("cmd.help"), Language.get("cmd.interact"))), false);

        Optional<Character> optCharacter = console.getCurrentRoom().getCharacter(argument);
        if (optCharacter.isEmpty()) {
            return new CommandResponse(Language.get("cmd.interact.err.noChar", argument), false);
        }
        Character character = optCharacter.get();
        TradeResult result = checkTrade(character);
        if (result.shouldReturn()) {
            return new CommandResponse(result.text(), false);
        }

        if (character.getStartNode() == null) return new CommandResponse(Language.get("cmd.interact.err.noDialogue", character.getName()), false);

        DialogueHandler.DialogueResult dialogueResult = console.getDialogueHandler().startDialogue(character);
        return switch (dialogueResult.status()) {
            case CONTINUE -> {
                console.setDialogueActive(true);
                yield new CommandResponse(dialogueResult.output(), false);
            }
            case ERROR, ENDED -> new CommandResponse(Language.get("cmd.interact.err.noDialogue", character.getName()), false);
        };
    }

    /**
     * Iterates through the character's active trades to see if the player has a matching item.
     * If a match is found, it processes the inventory exchange and fetches the associated dialogue.
     * @param character The NPC being interacted with.
     * @return A {@link TradeResult} indicating if a trade was successfully triggered or failed along with a return text.
     * Returns a false shouldReturn flag if no trades matched.
     */
    private TradeResult checkTrade(Character character) {
        log.info("Character {} has trades: {}", character.getName(), character.getTrades() != null);
        if (character.getTrades() != null) {
            for (Trade trade : character.getTrades().values()) {
                String tradeIn = trade.getTradeIn();
                Item tradeOut = trade.getTradeOut();

                Optional<Item> item = console.getInventory().removeItem(tradeIn);
                if (item.isPresent()) {
                    Optional<Inventory.InventoryFailure> failure = console.getInventory().addItem(tradeOut);
                    if (failure.isPresent()) {
                        console.getInventory().addItem(item.get());
                        switch (failure.get()) {
                            case INVENTORY_FULL -> {
                                return new TradeResult(true, Language.get("cmd.interact.err.trade.invFull"));
                            }
                            case NO_ITEM -> log.warn("{}'s trade for {} returned no item, this might be intentional", character.getName(), tradeIn);
                            case NOT_PICKUPABLE -> log.warn("{}'s trade for {} returned an unpickupable item, this might be intentional", character.getName(), tradeIn);
                        }
                        return new TradeResult(true, Language.get("cmd.interact.err.trade.failed"));
                    }
                }

                DialogueHandler.DialogueResult dialogueResult = console.getDialogueHandler().startDialogue(character, trade.getTradeDialogue());
                return switch (dialogueResult.status()) {
                    case CONTINUE -> {
                        console.setDialogueActive(true);
                        yield new TradeResult(true, dialogueResult.output());
                    }
                    case ERROR, ENDED -> new TradeResult(true, Language.get("cmd.interact.err.noDialogue", character.getName()));
                };
            }
        }
        return new TradeResult(false, null);
    }

    /**
     * An internal data carrier used to communicate the outcome of a trade evaluation.
     * @param shouldReturn {@code True} if a trade was triggered, {@code false} otherwise.
     * @param text The resulting text from the trade interaction.
     */
    private record TradeResult(boolean shouldReturn, String text) {}

    /**
     * Retrieves a summary of the interact command with aliases.
     * @return A localized short description string.
     */
    @Override
    public String getDescription() {
        return Language.get("cmd.interact.desc") + " " + Arrays.toString(Language.getArray("cmd.interact.aliases"));
    }

    /**
     * Retrieves the manual entry for the interact command.
     * @return A localized multi-line help string.
     */
    @Override
    public String getDetails() {
        return String.format("""
               %s
                    %s
               
               %s
                    %s""", Language.get("man.interact.cmd"), Language.get("man.interact.arg"),
                Language.get("man.example"), Language.get("man.interact.example"));
    }
}
