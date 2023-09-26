import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TextCharacter;

import java.io.IOException;
public class Game {
    private static Screen screen;
    // Default constructor
    public Game(){
        // This code initializes a Lanterna Terminal and a Screen
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null); // we don't need a cursor
            screen.startScreen(); // screens must be started
            screen.doResizeIfNecessary(); // resize screen if necessary
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void draw() throws IOException {
        screen.clear();
        screen.setCharacter(10, 10, TextCharacter.fromCharacter('X')[0]);
        screen.refresh();
    }
    public void run() throws IOException{
        draw();
    }
}
