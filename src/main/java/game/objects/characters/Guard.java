package game.objects.characters;

/*
 * Represents a guard which patrols the room, and will bring to player back to their cell if they are caught.
 * Has no chance of catching the player if they are asleep.
 */

/*
  TODO:
   - Delete this class, as in future versions there will only be a single NPC class (Enemy).
 */

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
