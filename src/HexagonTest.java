package src;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

/*
Hexagon random map theory

If we make a hexagon for each area where the length of each edge of the hexagon is no greater than 50 tiles and no less
than 30 tiles

Suppose that the first two points are given (lets label these point 1 and point 2) and that there is an edge between
them (this will be the case in-game, because the landing spot will be generated as a square where the two northernmost
points of the square will hold the next area and will have a distance of exactly 40 tiles)

The next point to be placed that creates an edge from the last point placed (in the first case, point 2) cannot have a
distance in tiles from point 1 greater than: the number of remaining points * 50. The edge also cannot cross any other edges.

The angle between two edges cannot be less than 45 degrees or greater than 270 degrees (as an integer. 270 degrees will force a
45-degree angle, decimal values close to 45 or 270 will be accepted)

The distance between any point and any other point not adjacent by edges has to be greater than 30 tiles. Additionally,
a line created from any point to an edge must not have a distance of 30 tiles from the point to the edge.

Each hexagon will require a total direction change of 360 degrees. The angles between each edge must add up to 720
degrees. In other words, each edge created must satisfy that: the sum of the angles between the remaining edges cannot
be less than 45 * the number of remaining points to add + 1
 */

public class HexagonTest {

    public static Point[] createHexagon(Point p1, Point p2, ArrayList<Polygon> hexes) {
        Point[] points = new Point[6];
        double degreeSum = 0;
        if(dist(p1, p2) > 50 || dist(p1, p2) < 30) {
            throw new IllegalArgumentException("these two points don't have a distance between 30 and 50 tiles. Dist = " + dist(p1, p2));
        }
        points[0] = p1;
        points[1] = p2;
        int curPoints = 2;
        Point[] result = generateHexagon(points, curPoints, 0.0, hexes);
        return result;
    }

    // O(1)
    public static double dist(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public static Point[] generateHexagon(Point[] points, int curPoints, double degreeSum, ArrayList<Polygon> hexes) {
        // Validation check
        if(curPoints < 2) {
            return null;
        }

        // Create a polar coordinate 5 degrees counter-clockwise close to the edge
        // This will be a test point to see if a hexagon can be generated here.
        int VecX = points[curPoints - 2].x - points[curPoints - 1].x;
        int VecY = points[curPoints - 2].y - points[curPoints - 1].y;
        double VecDir = Math.atan((double) VecY / VecX);
        if (VecX < 0 || (VecX == 0 && VecY > 0)) {
            VecDir = VecDir + Math.PI;
        }
        int r = 20;
        double theta = VecDir - Math.PI/36;
        int valX = (int)(r * Math.cos(theta)) + points[curPoints - 1].x;
        int valY = (int)(r * Math.sin(theta)) + points[curPoints - 1].y;
        Point p = new Point(valX, valY);
        for(Polygon poly : hexes) {
            if(poly.contains(p)) {
                return null;
            }
        }

        // Search for points
        ArrayList<Point> possible = getPossiblePoints(points, curPoints, degreeSum, hexes);
        System.out.println("Points: " + curPoints + ", number of possible next points: " + possible.size());
        Collections.shuffle(possible);
        for(int i = 0; i < possible.size(); i += 1) {
            points[curPoints] = possible.get(i);
            if (curPoints == 5) {
                return points;
            }
            int VecAX = points[curPoints - 2].x - points[curPoints - 1].x;
            int VecAY = points[curPoints - 2].y - points[curPoints - 1].y;
            int VecBX = points[curPoints].x - points[curPoints - 1].x;
            int VecBY = points[curPoints].y - points[curPoints - 1].y;
            double VecADir = Math.atan((double) VecAY / VecAX);
            double VecBDir = Math.atan((double) VecBY / VecBX);
            if (VecAX < 0 || (VecAX == 0 && VecAY < 0)) {
                VecADir = VecADir + Math.PI;
            }
            if (VecBX < 0 || (VecBX == 0 && VecBY < 0)) {
                VecBDir = VecBDir + Math.PI;
            }
            if (VecADir < 0) {
                VecADir += 2 * Math.PI;
            }
            if (VecADir < VecBDir) {
                VecBDir -= 2 * Math.PI;
            }
            Point[] result = generateHexagon(points, curPoints + 1, degreeSum + Math.abs(VecADir - VecBDir), hexes);
            if(result == null) {
                possible.remove(i);
            } else {
                return result;
            }
        }
        return null;
    }

    public static ArrayList<Point> getPossiblePoints(Point[] points, int curPoints, double degreeSum, ArrayList<Polygon> hexes) {
        // TODO: Change to polar coords
        //System.out.println("Scanning for point " + (curPoints + 1));
        ArrayList<Point> possiblePoints = new ArrayList<>();
        int VecX = points[curPoints - 2].x - points[curPoints - 1].x;
        int VecY = points[curPoints - 2].y - points[curPoints - 1].y;
        double VecDir = Math.atan((double) VecY / VecX);
        if (VecX < 0 || (VecX == 0 && VecY > 0)) {
            VecDir = VecDir + Math.PI;
        }
        //System.out.println("Change in values: (" + VecX + ", " + VecY + ") latest direction: " + VecDir * 180/Math.PI + ", Current sum of degrees: " + degreeSum);
        for(double i = VecDir - (Math.PI * 3)/2; i < VecDir - Math.PI/4; i += Math.PI/180) {
            for(int j = 30; j < 51; j += 1) {
                int valX = (int)(j * Math.cos(i)) + points[curPoints - 1].x;
                int valY = (int)(j * Math.sin(i)) + points[curPoints - 1].y;
                Point p = new Point(valX, valY);
                // Check if this point does not exist in the list
                // O(n) where n = number of points in the list
                if(possiblePoints.contains(p)) {
                    continue;
                }
                // Check integer conversion bug (in rare occasions the edge may be greater than 50 tiles)
                // O(1)
                if (dist(p, points[curPoints - 1]) > 50 || dist(p, points[curPoints - 1]) < 30) {
                    continue;
                }
                // Check if the remaining angles can be at least 45 degrees and at most 270 degrees
                // Also check if the angle goes above the required amount
                // O(1)
                int VecX2 = p.x - points[curPoints - 1].x;
                int VecY2 = p.y - points[curPoints - 1].y;
                double VecDir2 = Math.atan((double) VecY / VecX);
                if (VecX2 < 0 || (VecX2 == 0 && VecY2 > 0)) {
                    VecDir2 = VecDir2 + Math.PI;
                }
                double VecDir1 = VecDir;
                if (VecDir1 < 0) {
                    VecDir1 += 2 * Math.PI;
                }
                if (VecDir1 < VecDir2) {
                    VecDir2 -= 2 * Math.PI;
                }
                if (degreeSum + Math.abs(VecDir1 - VecDir2) > 4 * Math.PI) {
                    continue;
                }
                if (4 * Math.PI - (degreeSum + Math.abs(VecDir1 - VecDir2)) < (Math.PI/4) * (6.0 - (curPoints - 1)) ||
                        4 * Math.PI - (degreeSum + Math.abs(VecDir1 - VecDir2)) > (3 * Math.PI/2) * (6.0 - (curPoints - 1))) {
                    continue;
                }
                // Check if the distance between each of these points allows for:
                // a. The distance of the remaining edges is no greater than 50 for each edge
                // b. The distance of the remaining edges is no less than 30
                // O(1)
                if (dist(p, points[0]) > 50 * (6 - curPoints) || dist(p, points[0]) < 30) {
                    continue;
                }
                // Check if the distance between this point and another point is less than 30
                // O(1), max tests = 4
                boolean validPoint = true;
                for(int k = 0; k < curPoints - 1; k += 1) {
                    if(dist(p, points[k]) < 30) {
                        validPoint = false;
                        break;
                    }
                }
                if(!validPoint) {
                    continue;
                }
                // Check if this point is overlapping with a previously generated hexagon
                // O(n) where n = number of hexagons generated
                for(Polygon poly : hexes) {
                    if(poly.contains(p)) {
                        validPoint = false;
                        break;
                    }
                }
                if(!validPoint) {
                    continue;
                }
                // Check if this edge crosses any other edges in the hex
                // O(1), max tests = 3
                Line2D l1 = new Line2D.Float(p.x, p.y, points[curPoints - 1].x, points[curPoints - 1].y);
                for(int k = 0; k < curPoints - 2; k += 1) {
                    Line2D l2 = new Line2D.Float(points[k].x, points[k].y, points[k + 1].x, points[k + 1].y);
                    if (l1.intersectsLine(l2)) {
                        validPoint = false;
                        break;
                    }
                }
                if(!validPoint) {
                    continue;
                }
                // Check if this edge crosses any other hexagon's edge
                // O(n) where n = number of hexagons generated
                for(int k = 0; k < hexes.size(); k += 1) {
                    for(int l = 0; l < 6; l += 1) {
                        Point h1 = new Point(hexes.get(k).xpoints[l], hexes.get(k).ypoints[l]);
                        Point h2;
                        if(l == 5) {
                            h2 = new Point(hexes.get(k).xpoints[0], hexes.get(k).ypoints[0]);
                        } else {
                            h2 = new Point(hexes.get(k).xpoints[l+1], hexes.get(k).ypoints[l+1]);
                        }
                        Line2D l2 = new Line2D.Float(h1.x, h1.y, h2.x, h2.y);
                        if (l1.intersectsLine(l2) && !sharesPoints(p, points[curPoints - 1], h1, h2)) {
                            validPoint = false;
                            break;
                        }
                    }
                    if (!validPoint) {
                        break;
                    }
                }
                if(!validPoint) {
                    continue;
                }
                // All of those pass, you're good!
                possiblePoints.add(p);
            }
        }
        return possiblePoints;
    }

    public static boolean sharesPoints(Point p1, Point p2, Point p3, Point p4) {
        return (p1.equals(p2) || p1.equals(p3) || p1.equals(p4) || p2.equals(p3) || p2.equals(p4) || p3.equals(p4));
    }

    public static void printPoints(Point[] result) {
        if(result != null) {
            for(int i = 0; i < 6; i += 1) {
                System.out.print("Point " + (i + 1) + ": (" + result[i].x + ", " + result[i].y + ") \t");
            }
            System.out.println();
        } else {
            System.out.println("Result is null");
        }
    }

}
