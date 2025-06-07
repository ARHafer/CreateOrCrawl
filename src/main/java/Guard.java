public class Guard {
    private final String name, description;
    private Room room;

    public Guard(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String inspectString() {
        return "\n<" + name + ">\n" + description + "\n";
    }

    // Setters & Getters //
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return name;
    }
}
