package game.characters;

public class Player extends Character {
    public Player(String name) {
        super(name);
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n";
    }
}
