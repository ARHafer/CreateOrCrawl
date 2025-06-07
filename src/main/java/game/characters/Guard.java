package game.characters;

public class Guard extends NPC {
    private boolean sleeping;

    public Guard(String name, String description, boolean sleeping) {
        super(name, description);
        this.sleeping = sleeping;
    }

    @Override
    public String inspectString() {
        String sleepString = "";
        
        if (sleeping) {
            sleepString = "(Sleeping)";
        }
        
        return "\n<" + name + "> " + sleepString + "\n" + description + "\n";
    }
}
