package game.objects.items;

/*
 * Represents player's inventory. Handles the addition and removal of items while enforcing a maximum weight limit.
 */

import game.util.TextBank;

import java.util.ArrayList;
import java.util.HashMap;

/*
  TODO:
   - Refactor inventory class to be used by both the player and NPCs.
   - Make MAX_WEIGHT dynamic, changing based on the strength stat of the player or NPC.
       - e.g. ((STRENGTH + 1) / 2) * 15
 */

public class Inventory {
    private final HashMap<String, Item> items; // Since item names are entered in lowercase...
    private final ArrayList<String> itemNames; // Capitalized item names are stored here.
    private int currentWeight;

    private static final int MAX_WEIGHT = 25;

    public Inventory() {
        items = new HashMap<>();
        itemNames = new ArrayList<>();
        currentWeight = 0;
    }

    public void add(Item item) {
        if (item.getWeight() + currentWeight <= MAX_WEIGHT && item.isCarriable()) {
            currentWeight += item.getWeight();
            items.put(item.getName().toLowerCase(), item);
            itemNames.add(item.getName());
            item.setAdded(true); // Done so the Player class knows if the item was successfully added to the inventory
                                 // so that it can remove it from the room.

            if (item instanceof Key) {
                TextBank.FeedbackText.keyPickup(item.getName());
            } else {
                TextBank.FeedbackText.itemPickup(item.getName());
            }
        } else if (item.getWeight() + currentWeight > MAX_WEIGHT && item.isCarriable()) {
            TextBank.ErrorText.overMaxWeight();
        } else if (!item.isCarriable() || item.getWeight() > MAX_WEIGHT) {
            TextBank.ErrorText.tooHeavy();
        }
    }

    public void remove(Item item) {
        currentWeight -= item.getWeight();
        items.remove(item.getName().toLowerCase());
        itemNames.remove(item.getName());

        TextBank.FeedbackText.itemDrop(item.getName());
    }

    public Item get(String itemName) {
        return items.get(itemName.toLowerCase());
    }

    public boolean contains(String itemName) {
        return items.containsKey(itemName.toLowerCase());
    }

    @Override
    public String toString() {
        return "\n<Bag> (" + currentWeight + "/" + MAX_WEIGHT + " lbs)" + listItems() + "\n\n<Keychain>" + listKeys() + "\n";
    }

    private StringBuilder listItems() {
        StringBuilder list = new StringBuilder();
        ArrayList<String> onlyItems = new ArrayList<>();

        if (itemNames.isEmpty()) {
            list.append("\n   *Empty*");
        } else {
            for (String name : itemNames) {
                Item item = items.get(name.toLowerCase());

                if (!(item instanceof Key)) {
                    onlyItems.add(name);
                    list.append("\n - ").append(name).append(" (").append(item.weightString()).append(")");
                }
            }

            if (onlyItems.isEmpty()) {
                list.append("\n   *Empty*");
            }
        }

        return list;
    }

    private StringBuilder listKeys() {
        StringBuilder list = new StringBuilder();
        ArrayList<String> onlyKeys = new ArrayList<>();

        if (itemNames.isEmpty()) {
            list.append("\n   *Empty*");
        } else {
            for (String name : itemNames) {
                Item item = items.get(name.toLowerCase());

                if (item instanceof Key) {
                    onlyKeys.add(name);
                    list.append("\n - ").append(name);
                }
            }

            if (onlyKeys.isEmpty()) {
                list.append("\n   *Empty*");
            }
        }

        return list;
    }

    // Setters & Getters //
    public HashMap<String, Item> getItems() {
        return items;
    }
}
