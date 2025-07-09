package game.util;

/*
 * Contains nearly every line of text in the game. This serves two purposes, both to better separate class
 * responsibilities and making the eventual transition from command-line interface to GUI much simpler.
 *
 * Also streamlines the process of locating and editing specific text.
 */
public class TextBank {

    // Will make the switch from command-line interface to GUI easier.
    public static void print(Object text) {
        System.out.println(text);
    }

    /*
     * Text that displays when major in-game events happen. (e.g. Getting a game over.)
     */
    public static class EventText {

        // GameLoop //
        public static void intro() {
            print("""
                    
                    Welcome to "Create or Crawl - Version 0.1.0-alpha"
                    
                    You were recently arrested for rustling your neighbor's sheep - a crime punished by life in the Imperial
                    Dungeon. Although you're expected to spend the rest of your life in a dimly lit cell eating stale bread
                    and gruel, you have bigger aspirations. Armed with nothing but a makeshift bag and keychain, you begin
                    your text based escape.
                    
                    To begin, type "help" to view a list of all recognized commands.
                    """);
        }
        // GameLoop //

        // Player //
        public static void damageTaken() {
            print("""
                    
                    You get up, pain searing throughout your body, and frantically search your bag.
                    Luckily it seems as if they didn't notice it; None of your items have been confiscated.
                    
                    You lost a bit of HP as your head was slammed against the floor.
                    """);
        }

        public static void remainingHP(StringBuilder hpBar) {
            print("Remaining HP - " + hpBar + "\n");
        }

        public static void gameOver() {
            print("""
                    
                    As your head hits the floor once more, you don't find the strength to get back up.
                    You lie on the floor of your cell, slowly drifting out of consciousness, you realize that you
                    will never escape this dungeon.
                    
                    \t\t\t\t\tGAME OVER
                    """);
        }

        public static void victory() {
            print("""
                    
                    Stepping through the door, you're met with a ray of sunshine - something you thought you'd never see
                    again. You run off into the distance a free man, swearing to live the rest of your days on the right
                    side of the law.
                    
                    \t\t\t\t\tCONGRATULATIONS - YOU WIN!
                    """);
        }
        // Player //

        // NPC //
        public static void guardCapture() {
            print("""
                    As you're inspecting the object, you hear a booming voice coming from behind you yell,
                    "STOP RIGHT THERE, CRIMINAL SCUM!" You face behind you and see a guard, weapon drawn. Before you
                    can even yelp you're grabbed by your collar, dragged across the dungeon, and thrown back into
                    your cell, your head slamming against the cold, stone floor.""");
        }

        public static void maidCapture() {
            print("""
                    As you're inspecting the object, you feel somebody tapping on your shoulder. You face behind you
                    and see a maid with a stern look on their face. Before you can muster an apology the maid lifts
                    you up, hoisting you over their shoulder like a sack of potatoes. They carry you across the
                    dungeon and throw you back into your cell, your head slamming against the cold, stone floor.""");
        }
        // NPC //
    }

    /*
     * Text that displays as feedback for the player's actions. (e.g. Successful unlocking a door.)
     * Also contains the help menu, as it displays only due to direct player input.
     */
    public static class FeedbackText {

        // GameLoop //
        public static void helpMenu() {
            print("""
                    
                    <Help Menu>
                    "Look" ------------------------------ Output the information of the room you're currently in.
                    "North" / "East" / "South" / "West" - Enter a door in the corresponding direction.
                    "Inspect: [Object]" ----------------- Inspect a guard, maid, item, or door to learn more about it!
                    "Unlock: [Door]" -------------------- Unlock a given door, assuming you have the key.
                    "Pickup: [Item]" -------------------- Pick up a given item, adding it to your inventory and removing it from the room.
                    "Drop: [Item]" ---------------------- Drop a given item, removing it from your inventory and adding it to the room.
                    "Bag" ------------------------------- View your inventory.
                    "Status" ---------------------------- View your current health.
                    "Quit" / "Exit" --------------------- Exit the game.
                    """);
        }

        public static void exitGame() {
            print("\nExiting...\n");
        }
        // GameLoop //

        // Player //
        public static void enterDoor(String roomName) {
            print("\nYou open the door and enter the " + roomName + ".");
        }

        public static void unlockDoor(String keyName) {
            print("\nYou stick the " + keyName + " into the door's keyhole.\n*ker-chunk*\nThe door unlocks.\n");
        }
        // Player //

        // Inventory //
        public static void itemPickup(String itemName) {
            print("\nYou stuff the " + itemName + " into your bag.\n");
        }

        public static void keyPickup(String keyName) {
            print("\nYou slip the " + keyName + " onto your keychain.\n");
        }

        public static void itemDrop(String itemName) {
            print("\nYou set the " + itemName + " gently on the floor. Goodbye " + itemName + "!\n");
        }
        // Inventory //
    }

    /*
     * Text that displays when the player encounters an error, such as typing an unrecognized command or performing an
     * invalid action. (e.g. Unlocking an already unlocked door.)
     */
    public static class ErrorText {

        // GameLoop //
        public static void commandNotRecognized(String command) {
            print("\n\"" + command + "\" is not a recognized command. Type \"help\" to view a list of all valid commands.\n");
        }
        // GameLoop //

        // Player //
        public static void invalidCommandFormat(String command) {
            String object = switch (command) {
                case "inspect" -> "object";
                case "pickup", "drop" -> "item";
                case "unlock" -> "door";
                default -> "";
            };

            print("Invalid command format! Be sure to include \":\" after \"" + command + "\" and before the " + object +
                    " you wish to " + command + ".\nType \"help\" to view a list of all recognized commands.\n");
        }

        public static void noDoor() {
            print("\nThere isn't a door in that direction.\n");
        }

        public static void doorLocked() {
            print("\nYou try to open the door, but it won't budge. It must be locked.\n");
        }

        public static void doorAlreadyUnlocked() {
            print("""
                    
                    You attempt to unlock the door, but the key won't turn. After a few attempts, you realize that the
                    door is already unlocked. That's embarrassing.
                    """);
        }

        public static void objectNotFound(String command, String object) {
            switch (command) {
                case "inspect" -> print("\nNothing in this room or your inventory has the name \"" + object + ".\"\n");
                case "pickup" -> print("\nNo item in this room has the name \"" + object + ".\"\n");
                case "drop" -> print("\nNothing in your inventory has the name \"" + object + ".\"\n");
                case "unlock_door" -> print("\nNo door in this room has the name \"" + object + ".\"\n");
                case "unlock_key" -> print("\nNo key on your keychain unlocks the " + object + ".\n");
            }
        }
        // Player //

        // Inventory //
        public static void overMaxWeight() {
            print("\nYou're lugging around too much to pick that up! Drop something to lighten your load.\n");
        }

        public static void tooHeavy() {
            print("\nThat thing is far too heavy to even think about carrying around!\n");
        }
        // Inventory //
    }
}
