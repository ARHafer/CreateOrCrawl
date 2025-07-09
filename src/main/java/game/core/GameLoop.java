package game.core;

/*
 * Handles the game loop, including user input and command processing.
 */

import game.objects.characters.Player;
import game.util.TextBank;

import java.util.Scanner;

public class GameLoop {
    private final Player player;

    public GameLoop(Player player) {
        this.player = player;
    }

    public void play(Scanner scanner) {
        TextBank.EventText.intro();

        while (!player.hasEscaped()) {
            System.out.print("> ");
            String command = scanner.nextLine().toLowerCase().trim();

            processCommand(command);
        }

        System.exit(0);
    }

    private void processCommand(String command) {
        if (command.equals("help")) {
            TextBank.FeedbackText.helpMenu();
        } else if (command.equals("look")) {
            TextBank.print(player.getRoom().inspectString());
        } else if (command.equals("north") || command.equals("south") || command.equals("east") || command.equals("west")) {
            player.enterDoor(command);
        } else if (command.startsWith("inspect")) {
            player.inspect(command);
        } else if (command.startsWith("unlock")) {
            player.unlock(command);
        } else if (command.startsWith("pickup")) {
            player.pickup(command);
        } else if (command.startsWith("drop")) {
            player.drop(command);
        } else if (command.equals("bag")) {
            TextBank.print(player.getInventory());
        } else if (command.equals("status")) {
            TextBank.print(player);
        } else if (command.equals("quit") || command.equals("exit")) {
            TextBank.FeedbackText.exitGame();
            System.exit(0);
        } else {
            TextBank.ErrorText.commandNotRecognized(command);
        }
    }
}
