package com.wolfycz1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DialogueTest {
    DialogueNode dialogueNode;
    DialogueOption dialogueOption;
    DialogueHandler dialogueHandler;
    Character character;

    @BeforeEach
    void setUp() {
        dialogueNode = new DialogueNode("id", "text");
        dialogueOption = new DialogueOption("label", dialogueNode);
        dialogueHandler = new DialogueHandler();
        character = new Character("name");
        character.setStartNode(dialogueNode);
    }

    @Test
    void dialogueNode_addOption_ValidOption_True() {
        // Arrange
        // Act
        boolean returnValue = dialogueNode.addOption(dialogueOption);
        // Assert
        assertTrue(returnValue);
    }

    @Test
    void dialogueNode_addOption_NullOption_False() {
        // Arrange
        // Act
        boolean returnValue = dialogueNode.addOption(null);
        // Assert
        assertFalse(returnValue);
    }

    @Test
    void dialogueHandler_StartDialogue_DefualtNode_ResultContinue() {
        // Arrange
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.startDialogue(character);
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.CONTINUE, result.status());
        assertNotNull(result.output());
    }

    @Test
    void dialogueHandler_StartDialogue_NullCharacter_ResultEnded() {
        // Arrange
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.startDialogue(null);
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.ENDED, result.status());
    }

    @Test
    void dialogueHandler_StartDialogue_NullNode_ResultEnded() {
        // Arrange
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.startDialogue(character, null);
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.ENDED, result.status());
    }

    @Test
    void dialogueHandler_StartDialogue_SpecifiedNode_ResultContinue() {
        // Arrange
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.startDialogue(character, dialogueNode);
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.CONTINUE, result.status());
        assertNotNull(result.output());
    }

    @Test
    void dialogueHandler_ProcessInput_NotNumber_ResultError() {
        // Arrange
        dialogueHandler.startDialogue(character);
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.processInput("a");
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.ERROR, result.status());
    }

    @Test
    void dialogueHandler_ProcessInput_Zero_ResultError() {
        // Arrange
        dialogueHandler.startDialogue(character);
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.processInput("0");
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.ERROR, result.status());
    }

    @Test
    void dialogueHandler_ProcessInput_Negative_ResultError() {
        // Arrange
        dialogueHandler.startDialogue(character);
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.processInput("-1");
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.ERROR, result.status());
    }

    @Test
    void dialogueHandler_ProcessInput_OutOfBounds_ResultError() {
        // Arrange
        dialogueHandler.startDialogue(character);
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.processInput("33550336");
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.ERROR, result.status());
    }

    @Test
    void dialogueHandler_ProcessInput_ValidOption_ResultContinue() {
        // Arrange
        dialogueNode.addOption(dialogueOption);
        dialogueHandler.startDialogue(character);
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.processInput("1");
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.CONTINUE, result.status());
        assertNotNull(result.output());
    }

    @Test
    void dialogueHandler_ProcessInput_DialogueNotStarted_ResultEnded() {
        // Arrange
        // Act
        DialogueHandler.DialogueResult result = dialogueHandler.processInput("1");
        // Assert
        assertEquals(DialogueHandler.DialogueStatus.ENDED, result.status());
    }
}