package game.core;

/*
 * Handles the game loop, including user input and command processing.
 */

import game.objects.characters.Player;

import java.util.Scanner;

public class GameLoop {
    private final Player player;

    public GameLoop(Player player) {
        this.player = player;
    }

    public void play(Scanner scanner) {
        System.out.println("""
                
                Welcome to "Create or Crawl - Version 0.1.0-alpha"
                
                You were recently arrested for rustling your neighbor's sheep - a crime punished by life in the Imperial
                Dungeon. Although you're expected to spend the rest of your life in a dimly lit cell eating stale bread
                and gruel, you have bigger aspirations. Armed with nothing but a makeshift bag and keychain, you begin
                your text based escape.
                
                To begin, type "help" to view a list of all valid commands.
                """);

        while (!player.hasEscaped()) {
            System.out.print("> ");
            String command = scanner.nextLine().toLowerCase().trim();

            processCommand(command);
        }

        System.exit(0);
    }

    private void processCommand(String command) {
        if (command.equals("help")) {
            System.out.println(help());
        } else if (command.equals("look")) {
            System.out.println(player.getRoom().inspectString());
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
            System.out.println(player.getInventory());
        } else if (command.equals("status")) {
            System.out.println(player);
        } else if (command.equals("quit") || command.equals("exit")) {
            System.out.println("\nExiting the game...\n");
            System.exit(0);
        } else {
            System.out.println("\n\"" + command + "\" is not a recognized command. Type \"help\" to view a list of all valid commands.\n");
        }
    }

    private static String help() {
        return """
                
                <Commands>
                "Look" ------------------------------ Output the information of the room you're currently in.
                "North" / "East" / "South" / "West" - Enter a door in the corresponding direction.
                "Inspect: [Object]" ----------------- Inspect a guard, maid, item, or door to learn more about it!
                "Unlock: [Door]" -------------------- Unlock a given door, assuming you have the key.
                "Pickup: [Item]" -------------------- Pick up a given item, adding it to your inventory and removing it from the room.
                "Drop: [Item]" ---------------------- Drop a given item, removing it from your inventory and adding it to the room.
                "Bag" ------------------------------- View your inventory.
                "Status" ---------------------------- View your current health.
                "Quit" / "Exit" --------------------- Exit the game.
                """;
    }
}
