import java.util.ArrayList;

public class Room {
    private final String name, description;
    private final ArrayList<Guard> guards;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        guards = new ArrayList<>();
    }

    public void addGuard(Guard guard) {
        guards.add(guard);
    }

    @Override
    public String toString() {
        return "\n<" + name + ">\n" + description + "\n\nYou look around the room and see:\n" + listGuards() + "\n";
    }

    private StringBuilder listGuards() {
        StringBuilder list = new StringBuilder();
        int size = guards.size();

        // Code to ensure grammatical correctness //
        if (size > 1) {
            list.append(size).append(" Guards: ");
        } else if (size == 1) {
            list.append("1 Guard: ");
        } else {
            list.append("No Guards.");
        }

        if (size > 2) {
            int counter = size;

            for (Guard guard : guards) {
                if (counter > 2) {
                    list.append(guard.toString()).append(", ");
                    counter--;
                } else if (counter == 2) {
                    list.append(guard.toString()).append(", & ");
                    counter--;
                } else {
                    list.append(guard.toString()).append(".");
                }
            }
        } else if (size == 2) {
            list.append(guards.getFirst().toString()).append(" & ").append(guards.getLast().toString()).append(".");
        } else if (size == 1) {
            list.append(guards.getFirst().toString()).append(".");
        }

        return list;
    }
}
