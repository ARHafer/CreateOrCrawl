package game.characters;

public class Maid extends NPC {
    private boolean cleaning;

    public Maid(String name, String description, boolean cleaning) {
        super(name, description);
        this.cleaning = cleaning;
    }

    @Override
    public String inspectString() {
        String cleaningString = "";

        if (cleaning) {
            cleaningString = "(Cleaning)";
        }

        return "\n<" + name + "> " + cleaningString + "\n" + description + "\n";
    }
}
