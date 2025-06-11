package game.characters;

import game.environments.Door;
import game.environments.Room;
import game.items.Inventory;
import game.items.Item;
import game.items.Key;

import java.util.HashMap;
import java.util.Scanner;

public class Player extends Character {
    private final Inventory inventory;
    private HashMap<String, Guard> guards;
    private HashMap<String, Maid> maids;
    private HashMap<String, Item> items;

    public Player(String name) {
        super(name);
        inventory = new Inventory();
    }

    public void setRoom(Room room) {
        this.room = room;
        room.setExplored();
        guards = room.getGuards();
        maids = room.getMaids();
        items = room.getItems();
    }

    public void play(Scanner scanner) {
        boolean playing = true;

        while (playing) {
            System.out.print("> ");
            String command = scanner.nextLine().toLowerCase().trim();

            if (command.equals("help")) {
                System.out.println(help());
            } else if (command.equals("look")) {
                System.out.println(room.inspectString());
            } else if (command.equals("north") || command.equals("south") || command.equals("east") || command.equals("west")) {
                enterDoor(command);
            } else if (command.startsWith("inspect")) {
                inspect(command);
            } else if (command.startsWith("lock")) {
                lock(command);
            } else if (command.startsWith("unlock")) {
                unlock(command);
            } else if (command.startsWith("pickup")) {
                pickup(command);
            } else if (command.startsWith("drop")) {
                drop(command);
            } else if (command.equals("quit") || command.equals("exit")) {
                playing = false;
            } else {
                System.out.println("\n\"" + command + "\" is not a recognized command. Type \"help\" for a list of all valid commands.\n");
            }
        }

        System.out.println("\nExiting...\n");
        System.exit(0);
    }

    private void enterDoor(String direction) {
        Door door = room.getDoor(direction);

        if (door == null) {
            System.out.println("\nThere isn't a door in that direction!\n");
        } else if (door.isLocked()) {
            System.out.println("\nThe door won't budge, it must be locked!\n");
        } else {
            setRoom(door.getOtherRoom(room));
            System.out.println("\nYou open the door and enter the " + room.getName() + ".\n");
        }
    }

    private void inspect(String input) {
        String[] split = input.split(":", 2);
        String object = split[1].trim();

        if (guards.containsKey(object)) {
            Guard guard = guards.get(object);
            System.out.println(guard.inspectString());
            return;
        }

        if (maids.containsKey(object)) {
            Maid maid = maids.get(object);
            System.out.println(maid.inspectString());
            return;
        }

        if (items.containsKey(object)) {
            Item item = items.get(object);
            System.out.println(item.inspectString());
            item.setInspected();
            return;
        }

        if (inventory.contains(object)) {
            Item item = inventory.get(object);
            System.out.println(item.inspectString());
            return;
        }

        for (String direction : new String[]{"north", "south", "east", "west"}) {
            Door door = room.getDoor(direction);

            if (door != null && door.getName().equalsIgnoreCase(object)) {
                System.out.println(door.inspectString());
                return;
            }
        }

        System.out.println("\nNothing with that name is in this room or in your bag!\n");
    }

    private void lock(String input) {
        String[] split = input.split(":", 2);
        String doorName = split[1].trim();
        boolean doorFound = false;
        boolean keyFound = false;

        Door target;
        for (String direction : new String[]{"north", "south", "east", "west"}) {
            Door door = room.getDoor(direction);

            if (door != null && door.getName().equalsIgnoreCase(doorName)) {
                target = door;
                doorFound = true;

                for (Item item : inventory.getItems().values()) {
                    Key key = (Key) item;

                    if (key.getDoor().equals(target.getName())) {
                        keyFound = true;

                        if (!target.isLocked()) {
                            target.setLocked(true);
                            System.out.println("\nYou lock the door.\n");
                            return;
                        }

                        if (target.isLocked()) {
                            System.out.println("\nThe door is already locked!\n");
                        }

                        break;
                    }
                }
            }
        }

        if (!doorFound) {
            System.out.println("\nNo door in this room has that name.\n");
            return;
        }

        if (!keyFound) {
            System.out.println("\nYou don't have the key needed to lock the door!\n");
        }
    }

    private void unlock(String input) {
        String[] split = input.split(":", 2);
        String doorName = split[1].trim();
        boolean doorFound = false;
        boolean keyFound = false;

        Door target;
        for (String direction : new String[]{"north", "south", "east", "west"}) {
            Door door = room.getDoor(direction);

            if (door != null && door.getName().equalsIgnoreCase(doorName)) {
                target = door;
                doorFound = true;

                for (Item item : inventory.getItems().values()) {
                    Key key = (Key) item;

                    if (key.getDoor().equals(target.getName())) {
                        keyFound = true;

                        if (target.isLocked()) {
                            target.setLocked(false);
                            System.out.println("\nYou unlock the door.\n");
                            return;
                        }

                        if (!target.isLocked()) {
                            System.out.println("\nThe door is already unlocked!\n");
                        }

                        break;
                    }
                }
            }
        }

        if (!doorFound) {
            System.out.println("\nNo door in this room has that name.\n");
            return;
        }

        if (!keyFound) {
            System.out.println("\nYou don't have the key needed to unlock the door!\n");
        }
    }

    private void pickup(String input) {
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
            System.out.println("\nNo item in this room has that name.\n");
        }
    }

    private void drop(String input) {
        String[] split = input.split(":", 2);
        String itemName = split[1].trim();

        if (inventory.contains(itemName)) {
            Item item = inventory.get(itemName);
            inventory.remove(item);
            items.put(itemName, item);
            room.addItem(item);
        } else {
            System.out.println("\nYou're not carrying anything with that name.\n");
        }
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n";
    }

    private static String help() {
        return """
                
                <Help Menu>
                "Look" -------------------- Output the information of the room you're currently in.
                "North/East/South/West" --- Enter a door in the corresponding direction.
                "Inspect: [ObjectName]" --- Inspect a guard, maid, item, or door to learn more about it!
                "Lock/Unlock: [DoorName]" - Lock or unlock a door, given you have the key.
                "Pickup: [ItemName]" ------ Pick up an item.
                "Drop: [ItemName]" -------- Drop an item.
                "Quit/Exit" --------------- Exit the game.
                """;
    }
}
