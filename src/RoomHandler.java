package src;

public class RoomHandler {

    private Dungeon dungeon;
    private int x_size;
    private int y_size;

    public RoomHandler(int x_size, int y_size) {
        this.x_size = x_size;
        this.y_size = y_size;
    }

    public void createDungeon() {
        dungeon = new Dungeon("Test", x_size, y_size, 5, DungeonEntry.RANDOM);
        System.out.println(dungeon.toString());
    }

}
