package game.items;

import game.Inspectable;

import java.util.ArrayList;

public class Item implements Inspectable {
    protected final String name, description;
    private final int weight;
    private boolean carriable, hidden;
    private final ArrayList<Item> subitems; // Subitems are items hidden within another item.

    public Item(String name, String description, int weight, boolean carriable, boolean hidden) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.carriable = carriable;
        this.hidden = hidden;
        subitems = new ArrayList<>();
    }

    public void addSubitem(Item subitem) {
        subitems.add(subitem);
    }

    @Override
    public String inspectString() {
        String weightString;

        if (weight > 1) {
            weightString = weight + " lbs.";
        } else if (weight == 1) {
            weightString = weight + " lb.";
        } else {
            weightString = "Weightless";
        }

        return "\n<" + name + "> (" + weightString + ")\n" + description + "\n";
    }

    @Override
    public String toString() {
        return name;
    }

    // TEMPORARY - FOR TESTING PURPOSES //
    public String testString() {
        StringBuilder list = new StringBuilder();

        if (!subitems.isEmpty()) {
            list.append("\nSubitems:\n");
            for (Item subitem : subitems) {
                list.append(subitem.toString()).append('\n');
            }
        }

        return inspectString() + list;
    }
    // TEMPORARY - FOR TESTING PURPOSES //
}
