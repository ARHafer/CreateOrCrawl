package game.characters;

public class Maid extends NPC {
    public Maid(String name, String description) {
        super(name, description);
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n" + description + "\n";
    }
}
