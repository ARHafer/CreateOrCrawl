package game.objects.items;

/*
 * Represents a key that can unlock a given door. Keys are a specialized type of item, they are weightless, always
 * carriable, and cannot contain subitems.
 */

public class Key extends Item {
    private final String door; // String representation of the door the key unlocks.

    public Key(String name, String description, String door) {
        super(name, description, 0, true); // Keys are always weightless & carriable.
        this.door = door;
    }

    @Override
    public String inspectString() {
        return "\n<" + name + "> (Unlocks: " + door + ")\n" + description + "\n";
    }

    // Setters & Getters //
    public String getDoor() {
        return door;
    }
}
