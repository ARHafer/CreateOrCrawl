package game.items;

public class Key extends Item {
    private String door; // String representation of the door the key unlocks.

    public Key(String name, String description, boolean hidden, String door) {
        super(name, description, 0, true, hidden); // Keys are always weightless and carriable.
        this.door = door;
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n" + description;
    }
}
