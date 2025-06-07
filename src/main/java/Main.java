public class Main {
    public static void main(String[] args) {
        Room room = new Room("Room", "A room.");
        Guard g1 = new Guard("Guard 1", "The first guard.");
        Guard g2 = new Guard("Guard 2", "The second guard.");
        Guard g3 = new Guard("Guard 3", "The third guard.");

        System.out.println(g1.inspectString());

        // Testing listGuards() method in Room
        System.out.println(room.toString());
        room.addGuard(g1);
        System.out.println(room.toString());
        room.addGuard(g2);
        System.out.println(room.toString());
        room.addGuard(g3);
        System.out.println(room.toString());
    }
}
