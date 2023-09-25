import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TextCharacter;

import java.io.IOException;

public class Application {
    public static void main(String[] args) {

        // This code initializes a Lanterna Terminal and a Screen
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null); // we don't need a cursor
            screen.startScreen(); // screens must be started
            screen.doResizeIfNecessary(); // resize screen if necessary

            screen.clear();
            TextCharacter character = new TextCharacter('X');
            screen.setCharacter(10, 10, character);
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
