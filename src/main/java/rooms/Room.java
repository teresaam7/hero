package rooms;

import elements.Arena;

public class Room {
    private Arena arena;
    public Room(int width, int height, String mapFilePath){
        arena = new Arena(width, height);
        arena.loadMapFromFile(mapFilePath);
    }

    public Arena getArena() {
        return arena;
    }
}
