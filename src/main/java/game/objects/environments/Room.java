package game.objects.environments;

/*
 * Represents an individual room the player will explore. Every room also houses NPCs and items. Rooms are connected
 * via doors in the four cardinal directions.
 */

import game.Inspectable;
import game.objects.characters.Guard;
import game.objects.characters.Maid;
import game.objects.characters.NPC;
import game.objects.items.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class Room implements Inspectable {
    private final String name, description;
    private final HashMap<String, Guard> guards; //
    private final HashMap<String, Maid> maids;  // Since names are entered in lowercase...
    private final HashMap<String, Item> items; //
    private final ArrayList<String> guardNames, maidNames, itemNames; // Capitalized names are stored here.
    private Door north, south, east, west;
    private boolean explored;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        guards = new HashMap<>();
        guardNames = new ArrayList<>();
        maids = new HashMap<>();
        maidNames = new ArrayList<>();
        items = new HashMap<>();
        itemNames = new ArrayList<>();
        explored = false;
    }

    public void addNPC(NPC npc) {
        if (npc instanceof Guard) {
            guards.put(npc.getName().toLowerCase(), (Guard) npc);
            guardNames.add(npc.getName());
        } else if (npc instanceof Maid) {
            maids.put(npc.getName().toLowerCase(), (Maid) npc);
            maidNames.add(npc.getName());
        }

        npc.setRoom(this);
    }

    public void addItem(Item item) {
        items.put(item.getName().toLowerCase(), item);
        itemNames.add(item.getName());
        item.setRoom(this);
        item.setAdded(false);
    }

    public void removeItem(Item item) {
        items.remove(item.getName().toLowerCase());
        itemNames.remove(item.getName());
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n" + description + "\n\nYou look around the room and see:\n" +
                listObjects("Guard", guardNames) + "\n" + listObjects("Maid", maidNames) + "\n"
                + listObjects("Item", itemNames) + "\n" + listDoors() + "\n";
    }

    private StringBuilder listObjects(String objectName, ArrayList<String> objects) {
        StringBuilder list = new StringBuilder();
        int size = objects.size();

        // Code to ensure grammatical correctness //
        if (size > 1) {
            list.append(size).append(" ").append(objectName).append("s: ");
        } else if (size == 1) {
            list.append("1 ").append(objectName).append(": ");
        } else {
            list.append("No ").append(objectName).append("s.");
        }

        if (size > 2) {
            int counter = size;

            for (Object object : objects) {
                if (counter > 2) {
                    list.append(object).append(", ");
                    counter--;
                } else if (counter == 2) {
                    list.append(object).append(", & ");
                    counter--;
                } else {
                    list.append(object).append(".");
                }
            }
        } else if (size == 2) {
            list.append(objects.getFirst()).append(" & ").append(objects.getLast()).append(".");
        } else if (size == 1) {
            list.append(objects.getFirst()).append(".");
        }

        return list;
    }

    // Lists the name of the door before the other room has been explored, and the name of said other room after.
    private StringBuilder listDoors() {
        StringBuilder list = new StringBuilder();
        list.append("\n<Navigation>");

        if (north != null) {
            if (north.getOtherRoom(this).explored) {
                list.append("\nNorth - ").append(north.getOtherRoom(this).getName());
            } else {
                list.append("\nNorth - ").append(north.getName());
            }
        }
        if (south != null) {
            if (south.getOtherRoom(this).explored) {
                list.append("\nSouth - ").append(south.getOtherRoom(this).getName());
            } else {
                list.append("\nSouth - ").append(south.getName());
            }
        }
        if (east != null) {
            if (east.getOtherRoom(this).explored) {
                list.append("\nEast - ").append(east.getOtherRoom(this).getName());
            } else {
                list.append("\nEast - ").append(east.getName());
            }
        }
        if (west != null) {
            if (west.getOtherRoom(this).explored) {
                list.append("\nWest - ").append(west.getOtherRoom(this).getName());
            } else {
                list.append("\nWest - ").append(west.getName());
            }
        }

        return list;
    }

    // Setters & Getters //
    public void setDoor(Door door, String direction) {
        switch (direction.toLowerCase()) {
            case "north" -> {
                north = door;
                door.setRoom(this);
            }
            case "south" -> {
                south = door;
                door.setRoom(this);
            }
            case "east" -> {
                east = door;
                door.setRoom(this);
            }
            case "west" -> {
                west = door;
                door.setRoom(this);
            }
        }
    }

    public void setExplored() {
        explored = true;
    }

    public Door getDoor(String direction) {
        return switch (direction.toLowerCase()) {
            case "north" -> north;

            case "south" -> south;

            case "east" -> east;

            case "west" -> west;

            default -> null;
        };
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Guard> getGuards() {
        return guards;
    }

    public HashMap<String, Maid> getMaids() {
        return maids;
    }

    public HashMap<String, Item> getItems() {
        return items;
    }
}
