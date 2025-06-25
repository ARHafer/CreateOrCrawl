package game.objects.characters;

/*
 * Represents a maid which populates the room, and will bring to player back to their cell if they are caught.
 * Has no chance of catching the player if they are cleaning.
 */

/*
  TODO:
   - Delete this class, as in future versions there will only be a single NPC class (Enemy).
 */

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
