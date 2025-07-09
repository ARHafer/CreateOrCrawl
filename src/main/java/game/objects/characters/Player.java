package game.objects.characters;

/*
 * Represents the player character. Manages the player's hp, inventory, and actions.
 */

import game.objects.environments.Door;
import game.objects.environments.Room;
import game.objects.items.Inventory;
import game.objects.items.Item;
import game.objects.items.Key;
import game.util.TextBank;

import java.util.HashMap;

public class Player extends Character {
    private int hp; // (Health Points)
    private final Inventory inventory;
    private HashMap<String, Guard> guards; //
    private HashMap<String, Maid> maids;   // Cached data from the Room class for easy access
    private HashMap<String, Item> items;   //
    private Room start;

    private static final String[] DIRECTIONS = {"north", "south", "east", "west"};
    private static final String EXIT = "Exit";

    public Player(String name) {
        super(name);
        hp = 5;
        inventory = new Inventory();
    }

    protected void handleCapture() {
        hp--;

        if (hp > 0) {
            TextBank.EventText.damageTaken();
            TextBank.EventText.remainingHP(displayHPBar());
            setRoom(start);
        } else {
            TextBank.EventText.gameOver();

            System.exit(0);
        }
    }

    public void enterDoor(String direction) {
        Door door = room.getDoor(direction);

        if (door == null) {
            TextBank.ErrorText.noDoor();
        } else if (door.isLocked()) {
            TextBank.ErrorText.doorLocked();
        } else {
            setRoom(door.getOtherRoom(room));
            TextBank.FeedbackText.enterDoor(room.getName());
            TextBank.print(room.inspectString());
        }
    }

    public void inspect(String input) {
        if (!input.contains(":")) {
            TextBank.ErrorText.invalidCommandFormat("inspect");
            return;
        }

        String[] split = input.split(":", 2);
        String object = split[1].trim();

        if (guards.containsKey(object)) {
            Guard guard = guards.get(object);
            TextBank.print(guard.inspectString());
            checkForCapture();
            return;
        }

        if (maids.containsKey(object)) {
            Maid maid = maids.get(object);
            TextBank.print(maid.inspectString());
            checkForCapture();
            return;
        }

        if (items.containsKey(object)) {
            Item item = items.get(object);
            TextBank.print(item.inspectString());
            item.setInspected();
            checkForCapture();
            return;
        }

        if (inventory.contains(object)) {
            Item item = inventory.get(object);
            TextBank.print(item.inspectString());
            return; // No chance to be captured when inspecting items in the inventory.
        }

        for (String direction : DIRECTIONS) {
            Door door = room.getDoor(direction);

            if (door != null && door.getName().equalsIgnoreCase(object)) {
                TextBank.print(door.inspectString());
                checkForCapture();
                return;
            }
        }

        TextBank.ErrorText.objectNotFound("inspect", object);

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
            TextBank.ErrorText.invalidCommandFormat("unlock");
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
                                TextBank.ErrorText.doorAlreadyUnlocked();
                            }

                            if (target.isLocked()) {
                                target.setLocked(false);
                                TextBank.FeedbackText.unlockDoor(key.getName());
                                return;
                            }

                            break;
                        }
                    }
                }
            }
        }

        if (!doorFound) {
            TextBank.ErrorText.objectNotFound("unlock_door", doorName);
            return;
        }

        if (!keyFound) {
            TextBank.ErrorText.objectNotFound("unlock_key", doorName);
        }
    }

    public void pickup(String input) {
        if (!input.contains(":")) {
            TextBank.ErrorText.invalidCommandFormat("pickup");
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
            TextBank.ErrorText.objectNotFound("pickup", itemName);
        }
    }

    public void drop(String input) {
        if (!input.contains(":")) {
            TextBank.ErrorText.invalidCommandFormat("drop");
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
            TextBank.ErrorText.objectNotFound("drop", itemName);
        }
    }

    public boolean hasEscaped() {
        if (room.getName().equals(EXIT)) {
            TextBank.EventText.victory();
            return true;
        }

        return false;
    }


    @Override
    public String toString() {
        return "\n<" + name + ">\n" + "HP - " + displayHPBar() + "\n";
    }

    private StringBuilder displayHPBar() {
        StringBuilder hpBar = new StringBuilder();

        hpBar.append("[");
        hpBar.append("*".repeat(Math.max(0, hp)));
        hpBar.append("]");

        if (hp == 2) {
            hpBar.append(" !DANGER!");
        } else if (hp == 1) {
            hpBar.append(" !!CRITICAL!!");
        }

        return hpBar;
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
