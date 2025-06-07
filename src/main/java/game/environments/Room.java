package game.environments;

import game.Inspectable;
import game.characters.Character;
import game.characters.Guard;
import game.characters.Maid;
import game.items.Item;

import java.util.ArrayList;

public class Room implements Inspectable {
    private final String name, description;
    private final ArrayList<Character> characters;
    private final ArrayList<Item> items;
    private Door north, south, east, west;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        characters = new ArrayList<>();
        items = new ArrayList<>();
    }

    public void addCharacter(Character character) {
        characters.add(character);
        character.setRoom(this);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n" + description + "\n\nYou look around the room and see:\n" +
                listObjects("Guard", getGuards()) + "\n" + listObjects("Maid", getMaids()) + "\n"
                + listObjects("Item", items) + "\n" + listDoors() + "\n";
    }

    private StringBuilder listObjects(String objectName, ArrayList<?> objects) {
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
                    list.append(object.toString()).append(", ");
                    counter--;
                } else if (counter == 2) {
                    list.append(object.toString()).append(", & ");
                    counter--;
                } else {
                    list.append(object.toString()).append(".");
                }
            }
        } else if (size == 2) {
            list.append(objects.getFirst().toString()).append(" & ").append(objects.getLast().toString()).append(".");
        } else if (size == 1) {
            list.append(objects.getFirst().toString()).append(".");
        }

        return list;
    }

    /*
     * TO DO:
     * Add a boolean that marks a room as explored when the player enters.
     * If the room has been explored, list the room name, if not, just list "Unexplored Room"
     */
    private StringBuilder listDoors() {
        StringBuilder list = new StringBuilder();
        list.append("\n<Navigation>");

        if (north != null) {
            list.append("\nNorth - ").append(north.getOtherRoom(this).getName());
        }
        if (south != null) {
            list.append("\nSouth - ").append(south.getOtherRoom(this).getName());
        }
        if (east != null) {
            list.append("\nEast - ").append(east.getOtherRoom(this).getName());
        }
        if (west != null) {
            list.append("\nWest - ").append(west.getOtherRoom(this).getName());
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

    private ArrayList<Guard> getGuards() {
        ArrayList<Guard> guards = new ArrayList<>();

        for (Character character : characters) {
            if (character instanceof Guard) {
                guards.add((Guard) character);
            }
        }

        return guards;
    }

    private ArrayList<Maid> getMaids() {
        ArrayList<Maid> maids = new ArrayList<>();

        for (Character character : characters) {
            if (character instanceof Maid) {
                maids.add((Maid) character);
            }
        }

        return maids;
    }

    // TEMPORARY - FOR TESTING PURPOSES //
    public String testString() {
        return inspectString() + "\nInspectStrings + Subitems:\n" + listInspectStrings();
    }

    private StringBuilder listInspectStrings() {
        StringBuilder list = new StringBuilder();

        for (Character character : characters) {
            list.append(character.inspectString()).append("\n");
        }

        for (Item item : items) {
            list.append(item.testString()).append("\n");
        }

        return list;
    }
    // TEMPORARY - FOR TESTING PURPOSES //
}
