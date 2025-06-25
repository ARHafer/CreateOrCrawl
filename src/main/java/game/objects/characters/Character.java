package game.objects.characters;

/*
 * Contains methods & variables used by both the player and NPCs.
 */

import game.objects.environments.Room;

/*
  TODO:
   - Move "HP" variable from Player to Character.
   - Add an Inventory, as the player and NPCs will have one.
   - Implement stats used by Player and NPCs.
        - e.g. Attack (ATK), defense (DEF), & speed (SPD).
 */

public abstract class Character {
    protected final String name;
    protected Room room;

    public Character(String name) {
        this.name = name;
    }
}
