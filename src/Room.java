package src;

import java.util.ArrayList;
import java.util.Collections;

public class Room {

    private String id;
    private int x_size;
    private int y_size;
    private ArrayList<Coord> points;
    private int num_exits;
    private RoomType type;

    public Room(String id, int x_size, int y_size, RoomType type) {
        this.id = id;
        this.x_size = x_size;
        this.y_size = y_size;
        this.points = new ArrayList<>();
        this.type = type;
    }

    public Room(String id, int x_size, int y_size) {
        this.id = id;
        this.x_size = x_size;
        this.y_size = y_size;
        this.points = new ArrayList<>();
    }

    public void set_exits(int x) {
        num_exits = x;
    }

    public void add_points(Coord... coords) {
        Collections.addAll(points, coords);
    }

    public ArrayList<Coord> getCoords() {
        return points;
    }

    public void bestPossibleRoomType() {
        RoomType[] types = getPossibleRoomTypes();
        RoomLayout[] layouts = new RoomLayout[types.length];
        int numCoords = points.size();
        for(int i = 0; i < types.length; i += 1) {
            RoomLayout rl = new RoomLayout(types[i], x_size, y_size);
            if(rl.getGridSpaces() < numCoords) {
                layouts[i] = rl;
            }
        }

    }

    public RoomType[] getPossibleRoomTypes() {
        switch (x_size) {
            case 1:
                return switch (y_size) {
                    case 1 -> new RoomType[]{RoomType.BASIC_1X1};
                    case 2 -> new RoomType[]{RoomType.BASIC_1X2};
                    case 3 -> new RoomType[]{RoomType.BASIC_1X3};
                    case 4 -> new RoomType[]{RoomType.BASIC_1X4};
                    default -> throw new IllegalArgumentException();
                };
            case 2:
                return switch (y_size) {
                    case 1 -> new RoomType[]{RoomType.BASIC_1X2};
                    case 2 -> new RoomType[]{RoomType.BASIC_2X2, RoomType.L_SHAPE_2X2};
                    case 3 -> new RoomType[]{RoomType.BASIC_2X3, RoomType.L_SHAPE_2X3, RoomType.U_SHAPE_2X3,
                            RoomType.J_SHAPE_2X3, RoomType.T_SHAPE_2X3, RoomType.S_SHAPE_2X3,
                            RoomType.Z_SHAPE_2X3};
                    case 4 -> new RoomType[]{RoomType.BASIC_2X4, RoomType.L_SHAPE_2X4, RoomType.U_SHAPE_2X4,
                            RoomType.J_SHAPE_2X4};
                    default -> throw new IllegalArgumentException();
                };
            case 3:
                return switch (y_size) {
                    case 1 -> new RoomType[]{RoomType.BASIC_1X3};
                    case 2 -> new RoomType[]{RoomType.BASIC_2X3, RoomType.L_SHAPE_2X3, RoomType.U_SHAPE_2X3,
                            RoomType.J_SHAPE_2X3, RoomType.T_SHAPE_2X3, RoomType.S_SHAPE_2X3,
                            RoomType.Z_SHAPE_2X3};
                    case 3 -> new RoomType[]{RoomType.BASIC_3X3, RoomType.O_SHAPE_3X3, RoomType.U_SHAPE_3X3,
                            RoomType.L_SHAPE_3X3, RoomType.T_SHAPE_3X3, RoomType.DIAGONAL_3X3,
                            RoomType.PLUS_SHAPE_3X3};
                    default -> throw new IllegalArgumentException();
                };
            case 4:
                return switch (y_size) {
                    case 1 -> new RoomType[]{RoomType.BASIC_1X4};
                    case 2 -> new RoomType[]{RoomType.BASIC_2X4, RoomType.L_SHAPE_2X4, RoomType.U_SHAPE_2X4,
                            RoomType.J_SHAPE_2X4};
                    default -> throw new IllegalArgumentException();
                };
            default:
                throw new IllegalArgumentException();
        }
    }
}
