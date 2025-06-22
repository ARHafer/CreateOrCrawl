package game.characters;

public class Guard extends NPC {
    public Guard(String name, String description, boolean distracted) {
        super(name, description, distracted);
    }

    @Override
    public String inspectString() {
        String sleepString = "";
        
        if (distracted) {
            sleepString = "(Sleeping)";
        }
        
        return "\n<" + name + "> " + sleepString + "\n" + description + "\n";
    }
}
