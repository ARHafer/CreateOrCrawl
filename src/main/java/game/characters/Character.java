package game.characters;

import game.Inspectable;
import game.environments.Room;

public abstract class Character implements Inspectable {
    protected final String name;
    protected Room room;

    public Character(String name) {
        this.name = name;
    }
}
