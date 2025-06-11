package game.items;

public class Key extends Item {
    private final String door; // String representation of the door the key unlocks.

    public Key(String name, String description, int weight, String door) {
        super(name, description, weight, true); // Keys are always carriable.
        this.door = door;
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n" + description + "\n";
    }

    // Setters & Getters //
    public String getDoor() {
        return door;
    }
}
