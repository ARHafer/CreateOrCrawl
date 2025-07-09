package game.objects.characters;

/*
 * Contains methods & variables used by all NPCs.
 */

import game.Inspectable;
import game.objects.environments.Room;
import game.util.TextBank;

/*
  TODO:
   - Delete this class, as in future versions there will only be a single NPC class (Enemy).
 */

public abstract class NPC extends Character implements Inspectable {
    protected final String description;
    protected final boolean distracted; // Since "cleaning" and "sleeping" act identically, make it single value here.

    public NPC(String name, String description, boolean distracted) {
        super(name);
        this.description = description;
        this.distracted = distracted;
    }

    public void attemptCapture(Player player) {
        int rng = (int)(Math.random() * 100) + 1;

        // 20% chance for the player to be caught, assuming the NPC is not distracted.
        if (rng <= 20 && !this.distracted) {
            if (this instanceof Guard) {
                TextBank.EventText.guardCapture();
            } else if (this instanceof Maid) {
                TextBank.EventText.maidCapture();
            }

            player.handleCapture();
        }
    }

    // Setters & Getters
    public String getName() {
        return name;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
