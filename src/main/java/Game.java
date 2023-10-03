import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TextCharacter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {
    private static Screen screen;
    private Arena arena;
    private final ScheduledExecutorService monsterUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    private final int monsterUpdateInterval = 1000;
    // Default constructor
    public Game(int width, int height){
        // This code initializes a Lanterna Terminal and a Screen
        try {
            arena = new Arena(width, height);
            Terminal terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(width, height)).createTerminal();
            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);   // we don’t need a cursor
            screen.startScreen();             // screens must be started
            screen.doResizeIfNecessary();     // resize screen if necessary
            TerminalSize terminalSize = new TerminalSize(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void draw() throws IOException {
        screen.clear();
        arena.draw(screen.newTextGraphics());
        screen.refresh();
    }
    public void endGame(){
        System.out.println("Game Over!");
        System.out.println(0);
    }
    public void run() throws IOException {
        monsterUpdateExecutor.scheduleAtFixedRate(this::updateMonstersPeriodically, 0, monsterUpdateInterval, TimeUnit.MILLISECONDS);
        try {
            while (true) {
                draw();
                KeyStroke key = screen.readInput();
                processKey(key);
                if (key.getKeyType() == KeyType.EOF || (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q')) {
                    break;
                }
                if (arena.verifyMonsterCollisions()) {
                    endGame();
                    break;
                }
            }
        } finally {
            monsterUpdateExecutor.shutdown();
            screen.close();
        }
    }
    private void updateMonstersPeriodically() {
        arena.initializeMonsters();
        arena.updateMonsters();
        arena.moveMonsters();
    }
    private void processKey(KeyStroke key) {
        arena.processKey(key);
    }
}
