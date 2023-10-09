package com.teresaam7.hero;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.teresaam7.hero.rooms.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {
    private static Screen screen;
    private List<Room> rooms;
    private int currentRoomIdx;
    private Room currentRoom;
    private final ScheduledExecutorService monsterUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    private final int monsterUpdateInterval = 50;
    private boolean finalMessageDisplayed = false;
    // Default constructor
    public Game(int width, int height){
        // This code initializes a Lanterna Terminal and a Screen
        try {
            rooms = new ArrayList<>();
            rooms.add(new Room(width, height, "src/main/java/com/teresaam7/hero/materials/map1.txt"));
            // Start in the first room
            currentRoomIdx = 0;
            currentRoom = rooms.get(currentRoomIdx);


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
        currentRoom.getArena().draw(screen.newTextGraphics());
        screen.refresh();
    }
    public void endGame(boolean beatsGame) throws IOException{
        if (beatsGame) {
            finalMessage("Congratulations! You won!");

        } else {
            finalMessage("Game Over. You lost! Press 'R' to restart or 'Q' to quit");;
        }
    }
    public void run() throws IOException {
        monsterUpdateExecutor.scheduleAtFixedRate(this::updateMonstersPeriodically, 0, monsterUpdateInterval, TimeUnit.MILLISECONDS);
        boolean beatsGame = true;
        boolean restart = false;
        try {
            while (true) {
                if (restart) {
                    currentRoom.getArena().initialize();
                    finalMessageDisplayed = false;
                    restart = false;
                }
                draw();
                KeyStroke key = screen.readInput();
                processKey(key);
                if (key.getKeyType() == KeyType.EOF || (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q')) {
                    break;
                }
                if (!finalMessageDisplayed) {
                    if (currentRoom.getArena().verifyMonsterCollisions()) {
                        beatsGame = false;
                        endGame(beatsGame);
                        finalMessageDisplayed = true;
                        screen.refresh();
                        // Implementation of a delay of 2 seconds in order to display the final message
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }
                if (key.getKeyType() == KeyType.Character && (key.getCharacter() == 'R' || key.getCharacter() == 'r')) {
                    restart = true;
                }
            }
        } finally {
            monsterUpdateExecutor.shutdown();
            screen.close();
        }
    }
    // Method for the message that is displayed on the screen when the game ends
    public void finalMessage(String message) throws IOException{
        if (!finalMessageDisplayed){
            TextGraphics graphics = screen.newTextGraphics();
            graphics.setForegroundColor(TextColor.ANSI.WHITE);
            graphics.setBackgroundColor(TextColor.Factory.fromString("#7b68ee"));
            graphics.enableModifiers(SGR.BOLD);
            int w = (screen.getTerminalSize().getColumns() - message.length()) / 2;
            int h = screen.getTerminalSize().getRows() / 2;
            graphics.putString(w, h, message);
            screen.refresh();
            finalMessageDisplayed = true;
        }
    }
    private void updateMonstersPeriodically() {
        currentRoom.getArena().moveMonsters();
        currentRoom.getArena().updateMonsters();
    }
    private void processKey(KeyStroke key) {
        currentRoom.getArena().processKey(key);
    }
}
