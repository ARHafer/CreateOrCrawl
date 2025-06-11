package game.items;

import game.Inspectable;
import game.environments.Room;

import java.util.ArrayList;

public class Item implements Inspectable {
    protected final String name, description;
    protected final int weight;
    private final boolean carriable;
    private boolean added, inspected;
    private final ArrayList<Item> subitems; // Subitems are items hidden within another item.
    private Room room;

    public Item(String name, String description, int weight, boolean carriable) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.carriable = carriable;
        subitems = new ArrayList<>();
    }

    public void addSubitem(Item subitem) {
        subitems.add(subitem);
    }

    @Override
    public String inspectString() {
        return "\n<" + name + "> (" + weightString() + ")\n" + description + handleSubitems() + "\n";
    }

    String weightString() { // Package-private as this is only used here and in Inventory.
        String weightString;

        if (weight > 1) {
            weightString = weight + " lbs.";
        } else if (weight == 1) {
            weightString = weight + " lb.";
        } else {
            weightString = "Weightless";
        }

        return weightString;
    }

    /*
     * Because of how subitems are handled here (added to the room after their superitem is inspected), the "hidden"
     * variable is no longer needed.
     */
    private StringBuilder handleSubitems() {
        StringBuilder list = new StringBuilder();

        if (!subitems.isEmpty() && !inspected) {
            list.append("\n\nUpon further inspection of the ").append(name).append(", you find:");

            for (Item item : subitems) {
                list.append("\n - ").append(item.getName());
                room.addItem(item);
            }
        }

        return list;
    }

    // Setters & Getters //
    public void setRoom(Room room) {
        this.room = room;
    }

    public void setInspected() {
        inspected = true;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isCarriable() {
        return carriable;
    }

    public boolean isAdded() {
        return added;
    }
}
