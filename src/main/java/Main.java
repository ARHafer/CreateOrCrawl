import game.characters.Guard;
import game.environments.Door;
import game.environments.Room;
import game.items.Item;

public class Main {
    public static void main(String[] args) {
        Room room = new Room("Room", "A room.");
        Guard g1 = new Guard("Guard 1", "The first guard.");
        Guard g2 = new Guard("Guard 2", "The second guard.");
        Guard g3 = new Guard("Guard 3", "The third guard.");
        Door door = new Door("Door", "A door.", true);
        Item item = new Item("Item", "An item.", 0, true, false);

        System.out.println(door.inspectString());
        System.out.println(item.inspectString());

        // Testing listObjects() method in Room
        System.out.println(room.inspectString());
        room.addCharacter(g1);
        System.out.println(room.inspectString());
        room.addCharacter(g2);
        System.out.println(room.inspectString());
        room.addCharacter(g3);
        System.out.println(room.inspectString());
    }
}
