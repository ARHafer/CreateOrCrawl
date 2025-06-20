package game.items;

import java.util.ArrayList;
import java.util.HashMap;

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
                System.out.println("\nYou slip the " + item.getName() + " onto your keychain.\n");
            } else {
                System.out.println("\nYou stuff the " + item.getName() + " into your bag.\n");
            }
        } else if (item.getWeight() + currentWeight > MAX_WEIGHT && item.isCarriable()) {
            System.out.println("\nYou're lugging around too much to pick that up! Drop something to lighten your load!\n");
        } else if (!item.isCarriable() || item.getWeight() > MAX_WEIGHT) {
            System.out.println("\nThat thing is far too heavy to even think about carrying around!\n");
        }
    }

    public void remove(Item item) {
        currentWeight -= item.getWeight();
        items.remove(item.getName().toLowerCase());
        itemNames.remove(item.getName());

        System.out.println("\nYou drop the " + item.getName() + ". Goodbye " + item.getName() + "!\n");
    }

    public Item get(String itemName) {
        return items.get(itemName.toLowerCase());
    }

    public boolean contains(String itemName) {
        return items.containsKey(itemName.toLowerCase());
    }

    @Override
    public String toString() {
        return "\n<Bag> (" + currentWeight + "/25 lbs.)" + listItems() + "\n\n<Keychain>" + listKeys();
    }

    private StringBuilder listItems() {
        StringBuilder list = new StringBuilder();

        for (String name : itemNames) {
            Item item = items.get(name.toLowerCase());

            list.append("\n - ").append(name).append("(").append(item.weightString()).append(")");
        }

        return list;
    }

    private StringBuilder listKeys() {
        StringBuilder list = new StringBuilder();

        for (String name : itemNames) {
            Item item = items.get(name.toLowerCase());

            if (item instanceof Key) {
                list.append("\n - ").append(name).append("(").append(item.weightString()).append(")");
            }
        }

        return list;
    }

    // Setters & Getters //
    public HashMap<String, Item> getItems() {
        return items;
    }
}
