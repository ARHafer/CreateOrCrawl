package game.items;

import game.Inspectable;

public class Item implements Inspectable {
    protected final String name, description;
    private final int weight;
    private boolean carriable, hidden;

    public Item(String name, String description, int weight, boolean carriable, boolean hidden) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.carriable = carriable;
        this.hidden = hidden;
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
}
