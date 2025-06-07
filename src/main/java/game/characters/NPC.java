package game.characters;

public abstract class NPC extends Character {
    protected final String description;

    public NPC(String name, String description) {
        super(name);
        this.description = description;
    }
}
