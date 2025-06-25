package game.objects.characters;

/*
 * Represents the player character. Manages the player's health, inventory, and actions.
 */

import game.objects.environments.Door;
import game.objects.environments.Room;
import game.objects.items.Inventory;
import game.objects.items.Item;
import game.objects.items.Key;

import java.util.HashMap;

public class Player extends Character {
    private int health;
    private final Inventory inventory;
    private HashMap<String, Guard> guards; //
    private HashMap<String, Maid> maids;   // Cached data from the Room class for easy access
    private HashMap<String, Item> items;   //
    private Room start;

    private static final String[] DIRECTIONS = {"north", "south", "east", "west"};
    private static final String EXIT = "Exit";

    public Player(String name) {
        super(name);
        health = 5;
        inventory = new Inventory();
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

            setRoom(start);
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

    public void enterDoor(String direction) {
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

    public void inspect(String input) {
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

    public void unlock(String input) {
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

    public void pickup(String input) {
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

    public void drop(String input) {
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

    public boolean hasEscaped() {
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

    // Setters & Getters //
    public void setRoom(Room room) {
        this.room = room;
        room.setExplored();
        guards = room.getGuards();
        maids = room.getMaids();
        items = room.getItems();
    }

    public void setStart(Room room) {
        start = room;
    }

    public Room getRoom() {
        return room;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
