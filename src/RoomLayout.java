package src;

import java.util.List;
import java.util.ArrayList;

public class RoomLayout {
    private RoomType type;
    private int x_size;
    private int y_size;
    private int gridSpaces;
    private List<String[][]> layouts;
    private List<Byte[][]> entryVals;
    /*
    Entries:
    Bit 5: Top
    Bit 6: Right
    Bit 7: Bottom
    Bit 8: Left
     */
    public RoomLayout(RoomType type, int x_size, int y_size) {
        this.type = type;
        setGridSpaces(type);
    }

    private void setGridSpaces(RoomType type) {
        switch (type) {
            case BASIC_1X1:
                gridSpaces = 1;
                break;
            case BASIC_1X2:
                gridSpaces = 2;
                break;
            case BASIC_1X3, L_SHAPE_2X2:
                gridSpaces = 3;
                break;
            case BASIC_1X4, BASIC_2X2, L_SHAPE_2X3, J_SHAPE_2X3, T_SHAPE_2X3, S_SHAPE_2X3, Z_SHAPE_2X3:
                gridSpaces = 4;
                break;
            case L_SHAPE_2X4, L_SHAPE_3X3, U_SHAPE_2X3, J_SHAPE_2X4, T_SHAPE_3X3, PLUS_SHAPE_3X3:
                gridSpaces = 5;
                break;
            case BASIC_2X3, U_SHAPE_2X4, DIAGONAL_3X3:
                gridSpaces = 6;
                break;
            case U_SHAPE_3X3:
                gridSpaces = 7;
                break;
            case BASIC_2X4, O_SHAPE_3X3:
                gridSpaces = 8;
                break;
            case BASIC_3X3:
                gridSpaces = 9;
                break;
        }
    }

    public int getGridSpaces() {
        return gridSpaces;
    }

    private String[][][] getPossibleLayouts() {
        switch (type) {
            case BASIC_1X1:
                return new String[][][]{
                        {
                                {"X"}
                        }
                };
            case BASIC_1X2:
                switch (y_size) {
                    case 1:
                        return new String[][][]{
                                {
                                        {"X", "X"}
                                }
                        };
                    case 2:
                        return new String[][][]{
                                {
                                        {"X"},
                                        {"X"}
                                }
                        };
                }
            case BASIC_1X3:
                switch (y_size) {
                    case 1:
                        return new String[][][]{
                                {
                                        {"X", "X", "X"}
                                }
                        };
                    case 3:
                        return new String[][][]{
                                {
                                        {"X"},
                                        {"X"},
                                        {"X"}
                                }
                        };
                }
            case BASIC_1X4:
                switch (y_size) {
                    case 1:
                        return new String[][][]{
                                {
                                        {"X", "X", "X", "X"}
                                }
                        };
                    case 4:
                        return new String[][][]{
                                {
                                        {"X"},
                                        {"X"},
                                        {"X"},
                                        {"X"}
                                }
                        };
                }
            case BASIC_2X2:
                return new String[][][]{
                        {
                                {"X", "X"},
                                {"X", "X"}
                        }
                };
            case BASIC_2X3:
                switch (y_size) {
                    case 2:
                        return new String[][][]{
                                {
                                        {"X", "X", "X"},
                                        {"X", "X", "X"}
                                }
                        };
                    case 3:
                        return new String[][][]{
                                {
                                        {"X", "X"},
                                        {"X", "X"},
                                        {"X", "X"}
                                }
                        };
                }
            case BASIC_2X4:
                switch (y_size) {
                    case 2:
                        return new String[][][]{
                                {
                                        {"X", "X", "X", "X"},
                                        {"X", "X", "X", "X"}
                                }
                        };
                    case 4:
                        return new String[][][]{
                                {
                                        {"X", "X"},
                                        {"X", "X"},
                                        {"X", "X"},
                                        {"X", "X"}
                                }
                        };
                }
            case BASIC_3X3:
                return new String[][][]{
                        {
                                {"X", "X", "X"},
                                {"X", "X", "X"},
                                {"X", "X", "X"}
                        }
                };
            case L_SHAPE_2X2:
                return new String[][][]{
                        {
                                {"X", "_"},
                                {"X", "X"}
                        },
                        {
                                {"X", "X"},
                                {"X", "_"}
                        },
                        {
                                {"X", "X"},
                                {"_", "X"}
                        },
                        {
                                {"_", "X"},
                                {"X", "X"}
                        }
                };
            case L_SHAPE_2X3:
                switch (y_size) {
                    case 2:
                        return new String[][][]{
                                {
                                        {"_", "_", "X"},
                                        {"X", "X", "X"}
                                },
                                {
                                        {"X", "X", "X"},
                                        {"X", "_", "_"}
                                }
                        };
                    case 3:
                        return new String[][][]{
                                {
                                        {"X", "_"},
                                        {"X", "_"},
                                        {"X", "X"}
                                },
                                {
                                        {"X", "X"},
                                        {"_", "X"},
                                        {"_", "X"}
                                }
                        };
                }
            case L_SHAPE_2X4:
                switch (y_size) {
                    case 2:
                        return new String[][][]{
                                {
                                        {"X", "X", "X", "X"},
                                        {"X", "_", "_", "_"}
                                },
                                {
                                        {"_", "_", "_", "X"},
                                        {"X", "X", "X", "X"}
                                }
                        };
                    case 4:
                        return new String[][][]{
                                {
                                        {"X", "_"},
                                        {"X", "_"},
                                        {"X", "_"},
                                        {"X", "X"}
                                },
                                {
                                        {"X", "X"},
                                        {"_", "X"},
                                        {"_", "X"},
                                        {"_", "X"}
                                }
                        };
                }
            case L_SHAPE_3X3:
                return new String[][][]{
                        {
                                {"X", "_", "_"},
                                {"X", "_", "_"},
                                {"X", "X", "X"}
                        },
                        {
                                {"X", "X", "X"},
                                {"X", "_", "_"},
                                {"X", "_", "_"}
                        },
                        {
                                {"X", "X", "X"},
                                {"_", "_", "X"},
                                {"_", "_", "X"}
                        },
                        {
                                {"_", "_", "X"},
                                {"_", "_", "X"},
                                {"X", "X", "X"}
                        }
                };
            case U_SHAPE_2X3:
                break;
            case U_SHAPE_2X4:
                break;
            case U_SHAPE_3X3:
                break;
            case J_SHAPE_2X3:
                break;
            case J_SHAPE_2X4:
                break;
            case T_SHAPE_2X3:
                break;
            case T_SHAPE_3X3:
                break;
            case S_SHAPE_2X3:
                break;
            case Z_SHAPE_2X3:
                break;
            case O_SHAPE_3X3:
                break;
            case DIAGONAL_3X3:
                break;
            case PLUS_SHAPE_3X3:
                break;
            default:
                throw new IllegalArgumentException();
        }
        return null;
    }
}
