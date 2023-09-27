import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TextCharacter;

import java.io.IOException;
public class Game {
    private static Screen screen;
    private Arena arena;
    // Default constructor
    public Game(int width, int height){
        // This code initializes a Lanterna Terminal and a Screen
        try {
            arena = new Arena(width, height);
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
        arena.draw(screen.newTextGraphics());
        screen.refresh();
    }
    public void run() throws IOException{
        while (true){
            draw();
            // Method waits for a key stroke
            KeyStroke key = screen.readInput();
            processKey(key);
            if (key.getKeyType() == KeyType.EOF || (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q')){
                break;
            }
        }
        screen.close();

    }
    private void processKey(KeyStroke key) {
        arena.processKey(key);
    }
}
