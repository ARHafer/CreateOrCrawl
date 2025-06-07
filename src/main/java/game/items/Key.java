package game.items;

public class Key extends Item {
    private final String door; // String representation of the door the key unlocks.

    public Key(String name, String description, String door, boolean hidden) {
        super(name, description, 0, true, hidden); // Keys are always weightless and carriable.
        this.door = door;
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n" + description;
    }
}
