import game.MapHandler;
import game.characters.Player;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

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
            player.play(scanner);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
