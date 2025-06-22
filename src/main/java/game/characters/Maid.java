package game.characters;

public class Maid extends NPC {
    public Maid(String name, String description, boolean distracted) {
        super(name, description, distracted);
    }

    @Override
    public String inspectString() {
        String cleaningString = "";

        if (distracted) {
            cleaningString = "(Cleaning)";
        }

        return "\n<" + name + "> " + cleaningString + "\n" + description + "\n";
    }
}
