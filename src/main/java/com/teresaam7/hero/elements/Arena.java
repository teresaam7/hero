package com.teresaam7.hero.elements;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;
import java.util.*;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;
    private long lastMonsterSpawnTime;
    private long monsterDespawnInterval = 10000;
    private long monsterSpawnInterval = 100;
    private int score = 0;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public Arena(int width, int height){
        this.width = width;
        this.height = height;
        // Starting position in the middle
        this.hero = new Hero(width / 2, height / 2, 6);
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
    }
    // Method to initialize the arena in order to restart the game
    public void initialize() {
        hero = new Hero(width / 2, height / 2, 1);
        walls = createWalls();
        coins = createCoins();
        monsters = createMonsters();
        lastMonsterSpawnTime = 0;
        score = 0;
    }
    // Methods related with the elements.Hero's movement
    public boolean canHeroMove(Position position){
        int x = position.getX();
        int y = position.getY();
        if (x >= width || y >= height || x < 0 || y < 0){
            return false;
        }
        // Check if the position collides with a wall
        for (Wall wall: walls) {
            if(wall.getPosition().equals(position)){
                return false;
            }
        }
        return true;
    }
    public void moveHero(Position position) {
        if (canHeroMove(position)){
            hero.setPosition(position);
            retrieveCoins();
        }
    }
    public void processKey(KeyStroke key) {
        switch (key.getKeyType()){
            case ArrowRight:
                moveHero(hero.moveRight());
                break;
            case ArrowLeft:
                moveHero(hero.moveLeft());
                break;
            case ArrowDown:
                moveHero(hero.moveDown());
                break;
            case ArrowUp:
                moveHero(hero.moveUp());
                break;
        }
    }
    // Method related with the creation of walls
    private List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();
        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }
        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }
        return walls;
    }
    // Methods related with the Coins that are collected by the elements.Hero
    private List<Coin> createCoins() {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            coins.add(new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
        return coins;
    }
    public void retrieveCoins(){
        Iterator<Coin> iterator = coins.iterator();
        while (iterator.hasNext()) {
            Coin coin = iterator.next();
            if (coin.getPosition().equals(hero.getPosition())) {
                heroScore(5);
                iterator.remove();
                break;
            }
        }
    }
    public void heroScore(int coins){
        score+= coins;
    }
    // Methods related to the monsters that are disturbing our elements.Hero
    private List<Monster> createMonsters(){
        Random random = new Random();
        List<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            monsters.add(new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
        }
        return monsters;
    }
    private void spawnMonsterRandomly() {
        Random random = new Random();
        int x = random.nextInt(width - 2) + 1;
        int y = random.nextInt(height - 2) + 1;
        Position monsterPosition = new Position(x, y);
        if (canMonsterMove(monsterPosition)) {
            monsters.add(new Monster(x, y));
        }
    }
    private void despawnMonsters(long currentTime) {
        Iterator<Monster> iterator = monsters.iterator();
        // Check if it's time to despawn a new monster
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            if (!monster.isDespawned() && currentTime - monster.getSpawnTime() >= monsterDespawnInterval) {
                iterator.remove();
                monster.setDespawned(true);
            }
        }
    }
    public void updateMonsters() {
        long currentTime = System.currentTimeMillis();
        // Check if it's time to spawn a new monster
        if (currentTime - lastMonsterSpawnTime >= monsterSpawnInterval) {
            spawnMonsterRandomly();
            lastMonsterSpawnTime = currentTime;
        }
        despawnMonsters(currentTime);
    }
    public boolean canMonsterMove(Position position){
        int x = position.getX();
        int y = position.getY();
        if (x >= width || y >= height || x < 0 || y < 0){
            return false;
        }
        // Check if the position collides with a wall
        for (Wall wall: walls) {
            if(wall.getPosition().equals(position)){
                return false;
            }
        }
        return true;
    }
    public void moveMonsters() {
        Iterator<Monster> iterator = monsters.iterator();
        long currentTime = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            Position newPosition = monster.move();
            // Check if the new position collides with a wall
            if (!canMonsterMove(newPosition)) {
                iterator.remove();
            } else {
                monster.setPosition(newPosition);
            }
            // Check if it's possible to despawn the monster
            if (currentTime - monster.getSpawnTime() >= monsterDespawnInterval) {
                iterator.remove();
            }
        }
    }

    public boolean verifyMonsterCollisions(){
        for (Monster monster: monsters){
            if(monster.getPosition().equals(hero.getPosition())){
                hero.energyDecrease(1);
                if (hero.getEnergy().emptyEnergy())
                    return true;
            }
        }
        return false;
    }
    // Draw method for the scenario and all the components of the game
    public void draw(GenericTextGraphics graphics) throws IOException {
        // Drawing the arena
        graphics.setBackgroundColor(TextColor.Factory.fromString("#70d6ff"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        // Drawing the walls
        for (Wall wall : walls) {
            wall.draw(graphics);
        }
        // Drawing the coins
        for (Coin coin : coins){
            coin.draw(graphics);
        }
        // Drawing the monsters
        for (Monster monster : monsters) {
            monster.draw(graphics);
        }
        // Drawing the hero
        hero.draw(graphics);
        // Drawing the score on the screen
        graphics.setForegroundColor(TextColor.Factory.fromString("#e6e6fa"));
        graphics.setBackgroundColor(TextColor.Factory.fromString("#e79fc4"));
        graphics.putString(new TerminalPosition(2, 3), "Score: " + score);
    }
    // Method that loads the map of the game from the map1.txt file
    public void loadMapFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Random random = new Random();
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char symbol = line.charAt(col);
                    // Adjustment of the screen's size
                    int screenX = col + (width - line.length()) / 2;
                    int screenY = row + (height - 1 - row);
                    if (symbol == 'W') {
                        walls.add(new Wall(screenX, screenY));
                    } else if (symbol == 'H') {
                        hero.setPosition(new Position(screenX, screenY/2));
                    } else if (symbol == '$') {
                        coins.add(new Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
                    } else if (symbol == 'M') {
                        monsters.add(new Monster(screenX, screenY));
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
