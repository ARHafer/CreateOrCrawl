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
import java.util.Stack;

public class MapHandler extends DefaultHandler {
    private final HashMap<String, Room> rooms = new HashMap<>();
    private final Stack<Item> itemStack = new Stack<>();
    private Room currentRoom;
    private Player player;

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

                Item item = new Item(name, description, weight, carriable);

                /*
                 * In the .xml file, if an item is nested within the <item> element of another item it is treated as a
                 * subitem of the item whose <item> element it is nested within.
                 * This nesting is managed using a stack.
                 *
                 * When an item or key is processed it is pushed into the stack. When an <item> or <key> element ends,
                 * it is popped from the stack. If the stack is empty the item is added directly to the room.
                 * If the stack is NOT empty, the new item is treated as a subitem of the item whose <item> element
                 * nests it.
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

                Key key = new Key(name, description, door);

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
                currentRoom.addNPC(guard);
            }

            case "maid" -> {
                String name = attributes.getValue("name");
                String description = attributes.getValue("description");
                boolean cleaning = Boolean.parseBoolean(attributes.getValue("cleaning"));

                Maid maid = new Maid(name, description, cleaning);
                currentRoom.addNPC(maid);
            }

            case "player" -> {
                String name = attributes.getValue("name");

                player = new Player(name);
                player.setRoom(currentRoom);
                player.setStartingRoom(currentRoom);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("item") || qName.equals("key")) {
            itemStack.pop();
        }
    }

    // Setters & Getters //
    public Player getPlayer() {
        return player;
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
}
