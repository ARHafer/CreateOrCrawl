package game.objects.environments;

/*
 * Represents doors which connect two rooms. Doors can be locked, requiring a key to enter.
 */

import game.Inspectable;

/*
  TODO:
   - Implement other door/lock types that are unlocked via means other than a key.
        - e.g. Riddles (Either typed answer or item matching), switches in other rooms, clearing the room of enemies, etc.
 */

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
    void setRoom(Room room) { // Package-private as this is only used in Room.
        if (room1 == null) {
            room1 = room;
        } else {
            room2 = room;
        }
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Room getOtherRoom(Room room) {
        if (room1 == room) {
            return room2;
        } else {
            return room1;
        }
    }

    public String getName() {
        return name;
    }

    public boolean isLocked() {
        return locked;
    }
}
