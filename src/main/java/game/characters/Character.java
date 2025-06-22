package game.characters;

import game.environments.Room;

public abstract class Character {
    protected final String name;
    protected Room room;

    public Character(String name) {
        this.name = name;
    }
}
