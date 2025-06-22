package game.characters;

import game.environments.Door;
import game.environments.Room;
import game.items.Inventory;
import game.items.Item;
import game.items.Key;

import java.util.HashMap;
import java.util.Scanner;

public class Player extends Character {
    private int health;
    private Room startingRoom;
    private final Inventory inventory;
    private HashMap<String, Guard> guards;
    private HashMap<String, Maid> maids;
    private HashMap<String, Item> items;

    private static final String[] DIRECTIONS = {"north", "south", "east", "west"};
    private static final String EXIT = "Exit";

    public Player(String name) {
        super(name);
        health = 5;
        inventory = new Inventory();
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

        while (!winConditionMet()) {
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
            System.out.println(room.inspectString());
        } else if (command.equals("north") || command.equals("south") || command.equals("east") || command.equals("west")) {
            enterDoor(command);
        } else if (command.startsWith("inspect")) {
            inspect(command);
        } else if (command.startsWith("unlock")) {
            unlock(command);
        } else if (command.startsWith("pickup")) {
            pickup(command);
        } else if (command.startsWith("drop")) {
            drop(command);
        } else if (command.equals("bag")) {
            System.out.println(inventory);
        } else if (command.equals("status")) {
            System.out.println(this);
        } else if (command.equals("quit") || command.equals("exit")) {
            System.out.println("\nExiting the game...\n");
            System.exit(0);
        } else {
            System.out.println("\n\"" + command + "\" is not a recognized command. Type \"help\" to view a list of all valid commands.\n");
        }
    }

    private boolean winConditionMet() {
        if (room.getName().equals(EXIT)) {
            System.out.println("""
                    
                    Stepping through the door, you're met with a ray of sunshine - something you thought you'd never see
                    again. You run off into the distance a free man, swearing to live the rest of your days on the right
                    side of the law.
                    
                    \t\t\t\t\tCONGRATULATIONS - YOU WIN!
                    """);
            return true;
        }

        return false;
    }

    protected void handleCapture() {
        health--;

        if (health > 0) {
            System.out.println("""
                  
                    You get up, pain searing throughout your body, and frantically search your bag.
                    Luckily it seems as if they didn't notice it; None of your items have been confiscated.
   
                    You lost a bit of HP as your head was slammed against the floor.""");

            if (health > 2) {
                System.out.println("Remaining " + displayHealthBar() + "\n");
            } else if (health == 2) {
                System.out.println("Remaining " + displayHealthBar() + "\n");
            } else {
                System.out.println("Remaining " + displayHealthBar() + "\n");
            }

            setRoom(startingRoom);
        } else {
            System.out.println("""
                    
                    As your head hits the floor once more, you don't find the strength to get back up.
                    You lie on the floor of your cell, slowly drifting out of consciousness, you realize that you
                    will never escape this dungeon.
                    
                    \t\t\t\t\tGAME OVER
                    """);

            System.exit(0);
        }
    }

    private void enterDoor(String direction) {
        Door door = room.getDoor(direction);

        if (door == null) {
            System.out.println("\nThere isn't a door in that direction!\n");
        } else if (door.isLocked()) {
            System.out.println("\nThe door won't budge, it must be locked!\n");
        } else {
            setRoom(door.getOtherRoom(room));
            System.out.println("\nYou open the door and enter the " + room.getName() + ".");
            System.out.println(room.inspectString()); // After play-testing, I realized typing "look" after entering every
                                                      // room become tiresome, so the room inspectString will be displayed here.
        }
    }

    private void inspect(String input) {
        if (!input.contains(":")) {
            System.out.println("""
                    
                    Incorrect command format! Be sure to include ":" after "inspect".
                    Type "help" to view a list of all valid commands.
                    """);
            return;
        }

        String[] split = input.split(":", 2);
        String object = split[1].trim();

        if (guards.containsKey(object)) {
            Guard guard = guards.get(object);
            System.out.println(guard.inspectString());
            checkForCapture();
            return;
        }

        if (maids.containsKey(object)) {
            Maid maid = maids.get(object);
            System.out.println(maid.inspectString());
            checkForCapture();
            return;
        }

        if (items.containsKey(object)) {
            Item item = items.get(object);
            System.out.println(item.inspectString());
            item.setInspected();
            checkForCapture();
            return;
        }

        if (inventory.contains(object)) {
            Item item = inventory.get(object);
            System.out.println(item.inspectString());
            return; // No chance to be captured when inspecting items in the inventory.
        }

        for (String direction : DIRECTIONS) {
            Door door = room.getDoor(direction);

            if (door != null && door.getName().equalsIgnoreCase(object)) {
                System.out.println(door.inspectString());
                checkForCapture();
                return;
            }
        }

        System.out.println("\n\"" + object + "\" is not an object in this room or in your inventory!\n");

    }

    private void checkForCapture() {
        for (Guard guard : guards.values()) {
            guard.attemptCapture(this);
        }

        for (Maid maid : maids.values()) {
            maid.attemptCapture(this);
        }
    }

    private void unlock(String input) {
        if (!input.contains(":")) {
            System.out.println("""
                    
                    Incorrect command format! Be sure to include ":" after "unlock".
                    Type "help" to view a list of all valid commands.
                    """);
            return;
        }

        String[] split = input.split(":", 2);
        String doorName = split[1].trim();
        boolean doorFound = false;
        boolean keyFound = false;

        Door target;
        for (String direction : DIRECTIONS) {
            Door door = room.getDoor(direction);

            if (door != null && door.getName().equalsIgnoreCase(doorName)) {
                target = door;
                doorFound = true;

                for (Item item : inventory.getItems().values()) {
                    if (item instanceof Key key) {
                        if (key.getDoor().equals(target.getName())) {
                            keyFound = true;

                            if (!target.isLocked()) {
                                System.out.println("\nThat door is already unlocked!\n");
                            }

                            if (target.isLocked()) {
                                target.setLocked(false);
                                System.out.println("\nYou unlock the door with the " + key.getName() + ".\n");
                                return;
                            }

                            break;
                        }
                    }
                }
            }
        }

        if (!doorFound) {
            System.out.println("\n\"" + doorName + "\" is not a door in the room.\n");
            return;
        }

        if (!keyFound) {
            System.out.println("\nYou don't have the key needed to unlock that door!\n");
        }
    }

    private void pickup(String input) {
        if (!input.contains(":")) {
            System.out.println("""
                    
                    Incorrect command format! Be sure to include ":" after "pickup".
                    Type "help" to view a list of all valid commands.
                    """);
            return;
        }

        String[] split = input.split(":", 2);
        String itemName = split[1].trim();

        if (items.containsKey(itemName)) {
            Item item = items.get(itemName);
            inventory.add(item);

            if (item.isAdded()) {
                room.removeItem(item);
                items.remove(itemName);
            }

        } else {
            System.out.println("\n\"" + itemName + "\" is not an item in the room.\n");
        }
    }

    private void drop(String input) {
        if (!input.contains(":")) {
            System.out.println("""
                    
                    Incorrect command format! Be sure to include ":" after "drop".
                    Type "help" to view a list of all valid commands.
                    """);
            return;
        }

        String[] split = input.split(":", 2);
        String itemName = split[1].trim();

        if (inventory.contains(itemName)) {
            Item item = inventory.get(itemName);
            inventory.remove(item);
            items.put(itemName, item);
            room.addItem(item);
        } else {
            System.out.println("\n\"" + itemName + "\" is not an item in your inventory.\n");
        }
    }

    @Override
    public String toString() {
        return "\n<" + name + ">\n" + displayHealthBar() + "\n";
    }

    private StringBuilder displayHealthBar() {
        StringBuilder healthBar = new StringBuilder();

        healthBar.append("HP - [");
        healthBar.append("*".repeat(Math.max(0, health)));
        healthBar.append("]");

        if (health == 2) {
            healthBar.append(" !DANGER!");
        } else if (health == 1) {
            healthBar.append(" !!CRITICAL!!");
        }

        return healthBar;
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

    // Setters & Getters //
    public void setRoom(Room room) {
        this.room = room;
        room.setExplored();
        guards = room.getGuards();
        maids = room.getMaids();
        items = room.getItems();
    }

    public void setStartingRoom(Room room) {
        startingRoom = room;
    }
}
