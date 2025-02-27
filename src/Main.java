package src;

import java.awt.*;
import java.util.ArrayList;

// This is a test program which I have created in order to make a map of hexagons that connect to one another (still in development)
// Currently it's being used to test a dungeon creator for the game I'm trying to make

public class Main {
    public static void main(String[] args) {
        //testHexes();
        testDungeon();
    }
    public static void testHexes() {
        //MapGenerator.mapGenPrep();
        ArrayList<Polygon> generatedHexes = new ArrayList<>();
        Point p1 = new Point(0, 0);
        Point p2 = new Point(40, 0);
        Point[] points = HexagonTest.createHexagon(p1, p2, generatedHexes);
        HexagonTest.printPoints(points);
        Polygon p = new Polygon();
        for (Point pt : points) {
            p.addPoint(pt.x, pt.y);
        }
        generatedHexes.add(p);
        for(int i = 0; i < 5; i += 1) {
            boolean validEdge = false;
            while (!validEdge) {
                int edgePick = (int) (Math.random() * 5 + 1);
                int tries = 0;
                while(tries < 5) {
                    System.out.println("Tries: " + tries + ", Placing hexagon " + (i + 1));
                    Point n1;
                    Point n2;
                    // Try forwards first
                    if (edgePick == 5) {
                        n1 = new Point(generatedHexes.get(generatedHexes.size() - 1).xpoints[5], generatedHexes.get(generatedHexes.size() - 1).ypoints[5]);
                        n2 = new Point(generatedHexes.get(generatedHexes.size() - 1).xpoints[0], generatedHexes.get(generatedHexes.size() - 1).ypoints[0]);
                    } else {
                        n1 = new Point(generatedHexes.get(generatedHexes.size() - 1).xpoints[edgePick], generatedHexes.get(generatedHexes.size() - 1).ypoints[edgePick]);
                        n2 = new Point(generatedHexes.get(generatedHexes.size() - 1).xpoints[edgePick + 1], generatedHexes.get(generatedHexes.size() - 1).ypoints[edgePick + 1]);
                    }
                    points = HexagonTest.createHexagon(n1, n2, generatedHexes);
                    HexagonTest.printPoints(points);
                    // Try backwards if false, otherwise move on
                    if (points != null) {
                        validEdge = true;
                        break;
                    } else {
                        points = HexagonTest.createHexagon(n2, n1, generatedHexes);
                        HexagonTest.printPoints(points);
                        if (points != null) {
                            validEdge = true;
                            break;
                        } else {
                            if (edgePick == 5) {
                                edgePick = 1;
                            } else {
                                edgePick += 1;
                            }
                            tries += 1;
                        }
                    }
                }
            }
            Polygon pl = new Polygon();
            for (Point pt : points) {
                pl.addPoint(pt.x, pt.y);
            }
            generatedHexes.add(pl);
        }
    }
    public static void testDungeon() {
        RoomHandler rh = new RoomHandler(10, 10);
        rh.createDungeon();
    }
}