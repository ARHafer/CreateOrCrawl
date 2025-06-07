package game.environments;

import game.Inspectable;

public class Door implements Inspectable {
    private final String name, description;
    private Room room1, room2;
    private boolean locked;

    public Door(String name, String description, boolean locked) {
        this.name = name;
        this.description = description;
        this.locked = locked;
    }

    @Override
    public String inspectString() {
        String lockString;

        if (locked) {
            lockString = "Locked";
        } else {
            lockString = "Unlocked";
        }

        return "\n<" + name + "> (" + lockString + ")\n" + description + "\n";
    }

    // Setters & Getters //
    protected void setRoom(Room room) { // Protected as this is only used in Room.
        if (room1 == null) {
            room1 = room;
        } else {
            room2 = room;
        }
    }
}
