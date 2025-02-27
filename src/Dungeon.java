package src;

import java.util.*;

public class Dungeon {
    private String name;
    private int x_size;
    private int y_size;
    private ArrayList<Room> room_list;
    private String[][] layout;
    private int req_rooms;
    private DungeonEntry start_loc;

    public Dungeon(String name, int x_size, int y_size, int req_rooms, DungeonEntry start_loc) {
        this.name = name;
        this.x_size = x_size;
        this.y_size = y_size;
        this.req_rooms = req_rooms;
        room_list = new ArrayList<>();
        layout = new String[x_size + 8][y_size + 8];
        for(int i = 0; i < y_size + 8; i += 1) {
            for(int j = 0; j < x_size + 8; j += 1) {
                if(i - 4 < 0 || j - 4 < 0 || i + 4 >= y_size + 8 || j + 4 >= x_size + 8) {
                    layout[i][j] = "/";
                } else {
                    layout[i][j] = "_";
                }
            }
        }
        createStartRoom(start_loc);
        createMainRoute(req_rooms);
    }

    public void createMainRoute(int req_rooms) {
        // Step 1: Select adjacent tile to a room, set that as new room entry point
        Room start = room_list.get(0);
        Coord entry = getEntryPoint(start.getCoords(), Direction.ANY);

        // Step 2: Select a tile from this layout from the entry point that isn't already part of another room and is
        // adjacent through center (the entry point may also be selected). Ensure that the tile is accessible through no
        // more than 4 adjacent tiles
        Coord corner = getRoomCorner(entry);

        // Step 3: Make a basic-type room from the entrance to the selected point
        // Step 4: Remove all tiles part of different rooms
        layout[entry.getY()][entry.getX()] = "1";
        layout[corner.getY()][corner.getX()] = "2";
        ArrayList<Coord> roomCoords = new ArrayList<>();
        int xMax = Math.max(entry.getX(), corner.getX());
        int xMin = Math.min(entry.getX(), corner.getX());
        int yMax = Math.max(entry.getY(), corner.getY());
        int yMin = Math.min(entry.getY(), corner.getY());
        for(int i = yMin; i <= yMax; i += 1) {
            for(int j = xMin; j <= xMax; j += 1) {
                if(layout[i][j].equals("_")) {
                    roomCoords.add(new Coord(j, i));
                }
            }
        }
        Room r = new Room("1", xMax-xMin+1, yMax-yMin+1);

        // Step 5: Find the best match room for the tileset, removing all extra tiles
        //	5a: Take the dimensions of the basic room with a 2d array of the tiles and find what best matches
        //	5b: If removal of a tile is necessary to complete the room, remove that tile.
    }

    public Coord getRoomCorner(Coord entry) {
        ArrayList<Coord> checked = new ArrayList<>();
        Coord[] disallowed = {new Coord(entry.getX() + 4, entry.getY()),
                new Coord(entry.getX() - 4, entry.getY()),
                new Coord(entry.getX(), entry.getY() + 4),
                new Coord(entry.getX(), entry.getY() - 4)};
        checked.addAll(Arrays.asList(disallowed));
        ArrayList<Coord> possible = new ArrayList<>();
        Queue<Coord> q = new LinkedList<>();
        Queue<Integer> dists = new LinkedList<>();
        q.add(entry);
        dists.add(0);
        while(!q.isEmpty()) {
            Coord c = q.poll();
            int dist = dists.poll();
            Coord[] adj = {new Coord(c.getX() + 1, c.getY()),
                    new Coord(c.getX() - 1, c.getY()),
                    new Coord(c.getX(), c.getY() + 1),
                    new Coord(c.getX(), c.getY() - 1)};
            for(Coord a : adj) {
                if(layout[a.getY()][a.getX()].equals("_") && !checked.contains(a) && dist < 4) {
                    q.add(a);
                    dists.add(dist + 1);
                }
            }
            checked.add(c);
            possible.add(c);
        }
        return possible.get((int)(Math.random() * possible.size()));
    }

    public Coord getEntryPoint(ArrayList<Coord> coords, Direction direction) {
        ArrayList<Coord> entryPoints = new ArrayList<>();
        for(Coord c : coords) {
            Coord[] adj = {new Coord(c.getX() + 1, c.getY()),
                    new Coord(c.getX() - 1, c.getY()),
                    new Coord(c.getX(), c.getY() + 1),
                    new Coord(c.getX(), c.getY() - 1)};
            for(Coord a : adj) {
                if(layout[a.getY()][a.getX()].equals("_") && !entryPoints.contains(a)) {
                    entryPoints.add(a);
                }
            }
        }
        return entryPoints.get((int)(Math.random() * entryPoints.size()));
    }

    public void createStartRoom(DungeonEntry start_loc) {
        int x_loc = 0;
        int y_loc = 0;
        Room start_room = null;
        switch(start_loc) {
            case RANDOM:
                x_loc = (int)(Math.random() * x_size + 4);
                y_loc = (int)(Math.random() * y_size + 4);
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case TOP:
                x_loc = (int)(Math.random() * x_size + 4);
                y_loc = 4;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case RIGHT:
                x_loc = x_size + 4 - 1;
                y_loc = (int)(Math.random() * y_size + 4);
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case BOTTOM:
                x_loc = (int)(Math.random() * x_size + 4);
                y_loc = y_size + 4 - 1;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case LEFT:
                x_loc = 4;
                y_loc = (int)(Math.random() * y_size + 4);
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case ANY_SIDE:
                DungeonEntry[] possible = {DungeonEntry.TOP, DungeonEntry.RIGHT, DungeonEntry.BOTTOM, DungeonEntry.LEFT};
                int selection = (int)(Math.random() * 4);
                switch (possible[selection]) {
                    case TOP:
                        x_loc = (int)(Math.random() * x_size + 4);
                        y_loc = 4;
                        layout[y_loc][x_loc] = "0";
                        start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                        start_room.add_points(new Coord(x_loc, y_loc));
                        start_room.set_exits(1);
                        room_list.add(start_room);
                        break;
                    case RIGHT:
                        x_loc = x_size + 4 - 1;
                        y_loc = (int)(Math.random() * y_size + 4);
                        layout[y_loc][x_loc] = "0";
                        start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                        start_room.add_points(new Coord(x_loc, y_loc));
                        start_room.set_exits(1);
                        room_list.add(start_room);
                        break;
                    case BOTTOM:
                        x_loc = (int)(Math.random() * x_size + 4);
                        y_loc = y_size + 4 - 1;
                        layout[y_loc][x_loc] = "0";
                        start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                        start_room.add_points(new Coord(x_loc, y_loc));
                        start_room.set_exits(1);
                        room_list.add(start_room);
                        break;
                    case LEFT:
                        x_loc = 4;
                        y_loc = (int)(Math.random() * y_size + 4);
                        layout[y_loc][x_loc] = "0";
                        start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                        start_room.add_points(new Coord(x_loc, y_loc));
                        start_room.set_exits(1);
                        room_list.add(start_room);
                        break;
                    default:
                        throw new IllegalArgumentException("Dungeon Entry location not specified");
                }
                break;
            case TOP_CENTER:
                x_loc = x_size / 2 + 4;
                y_loc = 4;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case TOP_RIGHT:
                x_loc = x_size + 4 - 1;
                y_loc = 4;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case RIGHT_CENTER:
                x_loc = x_size + 4 - 1;
                y_loc = y_size / 2 + 4;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case BOTTOM_RIGHT:
                x_loc = x_size + 4 - 1;
                y_loc = y_size + 4 - 1;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case BOTTOM_CENTER:
                x_loc = x_size / 2 + 4;
                y_loc = y_size + 4 - 1;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case BOTTOM_LEFT:
                x_loc = 4;
                y_loc = y_size + 4 - 1;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case LEFT_CENTER:
                x_loc = 4;
                y_loc = y_size / 2 + 4;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case TOP_LEFT:
                x_loc = 4;
                y_loc = 4;
                layout[y_loc][x_loc] = "0";
                start_room = new Room("0", 1, 1, RoomType.BASIC_1X1);
                start_room.add_points(new Coord(x_loc, y_loc));
                start_room.set_exits(1);
                room_list.add(start_room);
                break;
            case LAST_EXIT:
                break;
            default:
                throw new IllegalArgumentException("Dungeon Entry location not specified");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < y_size + 8; i += 1) {
            for(int j = 0; j < x_size + 8; j += 1) {
                sb.append(layout[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
