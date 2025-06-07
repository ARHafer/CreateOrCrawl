package game.characters;

public class Guard extends NPC {
    public Guard(String name, String description) {
        super(name, description);
    }

    @Override
    public String inspectString() {
        return "\n<" + name + ">\n" + description + "\n";
    }
}
