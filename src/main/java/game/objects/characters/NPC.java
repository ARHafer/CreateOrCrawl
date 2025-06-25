package game.objects.characters;

/*
 * Contains methods & variables used by all NPCs.
 */

import game.Inspectable;
import game.objects.environments.Room;

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
                System.out.println("""
                        
                        As you're inspecting the object, you hear a booming voice coming from behind you yell,
                        "STOP RIGHT THERE, CRIMINAL SCUM!" You face behind you and see a guard, weapon drawn. Before you
                        can even yelp you're grabbed by your collar, dragged across the dungeon, and thrown back into
                        your cell, your head slamming against the cold, stone floor.""");
            } else if (this instanceof Maid) {
                System.out.println("""
                        
                        As you're inspecting the object, you feel somebody tapping on your shoulder. You face behind you
                        and see a maid with a stern look on their face. Before you can muster an apology the maid lifts
                        you up, hoisting you over their shoulder like a sack of potatoes. They carry you across the
                        dungeon and throw you back into your cell, your head slamming against the cold, stone floor.""");
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
