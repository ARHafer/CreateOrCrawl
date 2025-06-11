package game.characters;

import game.environments.Room;

public abstract class NPC extends Character {
    protected final String description;

    public NPC(String name, String description) {
        super(name);
        this.description = description;
    }

    // Setters & Getters
    public String getName() {
        return name;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
