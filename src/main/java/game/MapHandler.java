package game;

import game.characters.Guard;
import game.characters.Maid;
import game.characters.Player;
import game.environments.Door;
import game.environments.Room;
import game.items.Item;
import game.items.Key;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class MapHandler extends DefaultHandler {
    private final HashMap<String, Room> rooms = new HashMap<>();
    private final Stack<Item> itemStack = new Stack<>();
    private Room currentRoom;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "room" -> {
                String name = attributes.getValue("name");
                String description = attributes.getValue("description");

                currentRoom = new Room(name, description);
                rooms.put(name.toLowerCase(), currentRoom); // Names inserted to lowercase to ensure consistency.
            }

            case "door" -> {
                String direction = attributes.getValue("direction");
                String otherRoom = attributes.getValue("other_room");
                String name = attributes.getValue("name");
                String description = attributes.getValue("description");
                boolean locked = Boolean.parseBoolean(attributes.getValue("locked"));

                // Prevents duplicate, unlinked doors from being created.
                if (!rooms.containsKey(otherRoom.toLowerCase())) {
                    Door newDoor = new Door(name, description, locked);
                    currentRoom.setDoor(newDoor, direction);
                } else {
                    Door existingDoor = rooms.get(otherRoom.toLowerCase()).getDoor(getOppositeDirection(direction));
                    currentRoom.setDoor(existingDoor, direction);
                }
            }

            case "item" -> {
                String name = attributes.getValue("name");
                String description = attributes.getValue("description");
                int weight = Integer.parseInt(attributes.getValue("weight"));
                boolean carriable = Boolean.parseBoolean(attributes.getValue("carryable"));
                boolean hidden = Boolean.parseBoolean(attributes.getValue("hidden"));

                Item item = new Item(name, description, weight, carriable, hidden);

                /*
                 * In the .xml file, if an item is created but the <item> element doesn't end, any items added within
                 * the open element are intended to become subitems of that item. This is done here using a stack.

                 * When an item is added it is pushed into the stack. When the <item> element ends, the stack is
                 * emptied. So, if an item is created but the stack isn't empty, this means this current item is being
                 * created within an <item> element, making it a subitem of the initially processed item.
                 */
                if (itemStack.isEmpty()) {
                    currentRoom.addItem(item);
                } else {
                    Item superItem = itemStack.firstElement();
                    superItem.addSubitem(item);
                }

                itemStack.push(item);
            }

            case "key" -> {
                String name = attributes.getValue("name");
                String description = attributes.getValue("description");
                String door = attributes.getValue("door");
                boolean hidden = Boolean.parseBoolean(attributes.getValue("hidden"));

                Key key = new Key(name, description, door, hidden);

                if (itemStack.isEmpty()) {
                    currentRoom.addItem(key);
                } else {
                    Item superItem = itemStack.firstElement();
                    superItem.addSubitem(key);
                }

                itemStack.push(key);
            }

            case "guard" -> {
                String name = attributes.getValue("name");
                String description = attributes.getValue("description");
                boolean sleeping = Boolean.parseBoolean(attributes.getValue("sleeping"));

                Guard guard = new Guard(name, description, sleeping);
                currentRoom.addCharacter(guard);
            }

            case "maid" -> {
                String name = attributes.getValue("name");
                String description = attributes.getValue("description");
                boolean cleaning = Boolean.parseBoolean(attributes.getValue("cleaning"));

                Maid maid = new Maid(name, description, cleaning);
                currentRoom.addCharacter(maid);
            }

            case "player" -> {
                String name = attributes.getValue("name");

                Player player = new Player(name);
                currentRoom.addCharacter(player);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("item") || qName.equals("key")) {
            itemStack.pop();
        }
    }

    private String getOppositeDirection(String direction) {
        return switch (direction) {
            case "north" -> "south";

            case "south" -> "north";

            case "east" -> "west";

            case "west" -> "east";

            default -> direction;
        };
    }

    // TEMPORARY - FOR TESTING PURPOSES //
    public void test(Scanner scanner) {
        String room = scanner.nextLine().toLowerCase();

        if (rooms.containsKey(room)) {
            System.out.println(rooms.get(room).testString());
        } else {
            System.out.println("No room with that name exists.");
        }
    }
    // TEMPORARY - FOR TESTING PURPOSES //
}
