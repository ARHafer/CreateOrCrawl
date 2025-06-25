import game.MapHandler;
import game.core.GameLoop;
import game.objects.characters.Player;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/*
  TODO (In order of addition):
   1. Combat System
        - Add "BattleManager" class.
        - Add "Enemy" class.
        - Add "Weapon" class.
        - Implement player & enemy stats.
   2. Door/Lock Variants
        - Add "RiddleDoor", "ItemDoor", "LeverDoor", & "EnemyDoor" classes.
        - or -
        - Add "RiddleLock", "ItemLock", "LeverLock", & "EnemyLock" classes.
   3. Save/Load System
   4. GUI w/ Minimap
   5. Dungeon Editor
 */

public class Main {
    public static void main(String[] args) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        InputStream mapFile = Main.class.getResourceAsStream("test_input.xml");

        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            MapHandler handler = new MapHandler();
            saxParser.parse(mapFile, handler);

            Scanner scanner = new Scanner(System.in);
            Player player = handler.getPlayer();
            GameLoop game = new GameLoop(player);

            game.play(scanner);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
