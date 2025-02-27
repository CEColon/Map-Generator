package src;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class MapGenerator {

    public static final int SIZE = 4096;

    public static void mapGenPrep() {
        JFrame map = new JFrame("Map Gen");
        MapPanel pnl = new MapPanel();
        map.add(pnl, BorderLayout.CENTER);
        map.setSize(SIZE*1, SIZE*1);
        map.pack();
        map.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        map.setVisible(true);
        choice(pnl);
    }

    public static void choice(MapPanel pnl) {
        Scanner s = new Scanner(System.in);
        System.out.print("Would you like to (L)oad or (C)reate a map? ");
        String decision = s.nextLine();
        boolean decisionValid = false;
        while(!decisionValid) {
            if(decision.toUpperCase().charAt(0) == 'L') {
                System.out.print("What is the filename of the map you would like to load? ");
                String filename = s.nextLine();
                decisionValid = true;
                loadMap(filename, pnl);
            }
            else if(decision.toUpperCase().charAt(0) == 'C') {
                decisionValid = true;
                worldGenerationSettings(pnl, s);
            }
            else {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void loadMap(String filename, MapPanel pnl) {
        int[][] elevations = new int[SIZE][SIZE];
        char[][] biomes = new char[SIZE][SIZE];
        char[][] regions = new char[SIZE][SIZE];

        JSONParser parser = new JSONParser();
        String line = null;
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader br = new BufferedReader(reader);
            System.out.println("Reading file...");
            while((line = br.readLine()) != null) {
                Object o = parser.parse(line);
                JSONObject point = (JSONObject) o;
                int i = Integer.parseInt((String)point.get("y"));
                int j = Integer.parseInt((String)point.get("x"));
                System.out.println("Plotting point (" + i + ", " + j + ")");
                elevations[i][j] = Integer.parseInt((String)point.get("elevation"));
                String biome = (String)(point.get("biome"));
                biomes[i][j] = biome.charAt(0);
                String region = (String)point.get("region");
                regions[i][j] = region.charAt(0);
                pnl.importBiomes(biomes);
                pnl.importList(elevations);
                pnl.importRegions(regions);
            }
            System.out.println("Done");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void worldGenerationSettings(MapPanel pnl, Scanner s) {
        boolean decision2Valid = false;
        System.out.println("What generation settings would you like? (N)ew, (o)ld");
        while(!decision2Valid) {
            String decision2 = s.nextLine();
            if(decision2.toUpperCase().charAt(0) == 'N') {
                makeRoadMap(pnl); // The new version currently being worked on (uses roads for now, but will be changed to use hexagons, being tested in Main.java)
                decision2Valid = true;
            } else if(decision2.toUpperCase().charAt(0) == 'O') {
                makeAmplifiedMap(pnl); // this is the old version of the generator (It's not great, but it works), Biome overlay on regular map is disabled for testing
                decision2Valid = true;
            } else {
                System.out.println("Not a valid choice.");
            }
        }
    }

    public static void makeRoadMap(MapPanel pnl) {
        int[][] roads = new int[SIZE][SIZE];
        char[][] regions = new char[SIZE][SIZE];
        ArrayList<Integer> xVals = new ArrayList<Integer>();
        ArrayList<Integer> yVals = new ArrayList<Integer>();
        System.out.println("Initializing map...");
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                roads[i][j] = -200;
            }
        }
        System.out.println("Placing a random division point...");
        int divX = SIZE/2;
        int divY = SIZE/2;
        for(int i = divX-1; i <= divX+1; i++) {
            for(int j = divY-1; j <= divY+1; j++) {
                roads[i][j] = 50;
            }
        }
        System.out.println("Connecting division to random spawn...");
        int curX = divX;
        int curY = divY;
        double actX = divX;
        double actY = divY;
        int dir = 270;
        for(int k = 0; k < 300; k++) {
            actX += 3 * Math.cos(dir * (Math.PI/180));
            actY -= 3 * Math.sin(dir * (Math.PI/180));
            curX = (int)actX;
            curY = (int)actY;
            for(int i = curX-1; i <= curX+1; i++) {
                for(int j = curY-1; j <= curY+1; j++) {
                    roads[i][j] = (300-k)/6;
                    regions[i][j] = 'r';
                }
            }
            if(k < 250) {
                dir += (int)(Math.random() * 12 - 6);
            } else {
                if(dir >= 90 && dir < 225) {
                    dir += 5;
                } else if (dir >= 225 && dir < 260) {
                    dir += 2;
                } else if (dir >= 260 && dir < 270) {
                    dir += 1;
                } else if ((dir < 90 && dir > 0) || (dir < 360 && dir > 315)) {
                    dir -= 5;
                } else if (dir <= 315 && dir > 280) {
                    dir -= 2;
                } else if (dir <= 280 && dir > 270) {
                    dir -= 1;
                }
            }
            if (dir > 359) {
                dir -= 360;
            } else if (dir < 0) {
                dir += 360;
            }
        }

        System.out.println("Making many more divisions...");
        for(int d = 0; d < 8; d++) {
            curX = divX;
            curY = divY;
            actX = divX;
            actY = divY;
            dir = (int)(Math.random() * 270) - 45;
            if (dir < 0) {
                dir += 360;
            }
            for (int k = 0; k < 600; k++) {
                actX += 3 * Math.cos(dir * (Math.PI / 180));
                actY -= 3 * Math.sin(dir * (Math.PI / 180));
                curX = (int) actX;
                curY = (int) actY;
                for (int i = curX - 1; i <= curX + 1; i++) {
                    for (int j = curY - 1; j <= curY + 1; j++) {
                        roads[i][j] = 50;
                        regions[i][j] = 'r';
                    }
                }
                if (k < 200) {
                    dir += (int)(Math.random() * 12 - 6);
                } else if (k < 400) {
                    dir += (int) (Math.random() * 10 - 5);
                } else {
                    dir += (int) (Math.random() * 8 - 4);
                }
                if (dir > 359) {
                    dir -= 360;
                } else if (dir < 0) {
                    dir += 360;
                }
            }
        }
        pnl.importList(roads);
        pnl.importRegions(regions);
        generateTerrain(roads, pnl, 50);
        System.out.println("Done");
    }

    public static void makeAmplifiedMap(MapPanel pnl) {
        int[][] elevations = new int[SIZE][SIZE];
        ArrayList<Integer> xvals = new ArrayList<Integer>();
        ArrayList<Integer> yvals = new ArrayList<Integer>();
        System.out.println("Initializing map...");
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                elevations[i][j] = -200;
            }
        }
        System.out.println("Setting up random mountains...");
        for(int i = 0; i < (int)(Math.random() * 100) + 20; i++) {
            int x = (int)(Math.random()*SIZE/4*3) + SIZE/8;
            int y = (int)(Math.random()*SIZE/4*3) + SIZE/8;
            elevations[x][y] = 120;
            xvals.add(x);
            yvals.add(y);
        }
        System.out.println("Setting up random hills...");
        for(int i = 0; i < (int)(Math.random() * 50) + 5; i++) {
            int x = (int)(Math.random()*SIZE/4*3) + SIZE/8;
            int y = (int)(Math.random()*SIZE/4*3) + SIZE/8;
            elevations[x][y] = (int)(Math.random()*30 + 60);
            xvals.add(x);
            yvals.add(y);
        }

        generateTerrain(elevations, pnl, 120);

        //printlist(elevations);
        pnl.importList(elevations);
        makeBiomes(pnl);
        pnl.createRegions(elevations, xvals, yvals);
    }

    public static void generateTerrain(int[][] list, MapPanel pnl, int startval) {
        System.out.println("Generating Terrain...");
        int[][] elevations = list;
        boolean creating = true;
        int topval = startval;
        while(creating) {
            int undetermined = 0;
            for(int i = 0; i < SIZE; i++) {
                for(int j = 0; j < SIZE; j++) {
                    if(elevations[i][j] < topval - 5) {
                        ArrayList<Integer> adjacent = new ArrayList<Integer>();
                        if(i > 0) {
                            adjacent.add(elevations[i-1][j]);
                        }
                        if(i < SIZE-1) {
                            adjacent.add(elevations[i+1][j]);
                        }
                        if(j > 0) {
                            adjacent.add(elevations[i][j-1]);
                        }
                        if(j < SIZE-1) {
                            adjacent.add(elevations[i][j+1]);
                        }
                        for(int k = adjacent.size()-1; k > -1; k--) {
                            if(adjacent.get(k) < topval) {
                                adjacent.remove(k);
                            }
                        }
                        switch(adjacent.size()) {
                            case 0:
                                undetermined++;
                                break;
                            case 1:
                                elevations[i][j] = picknumber(adjacent.get(0));
                                break;
                            case 2:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1));
                                break;
                            case 3:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1), adjacent.get(2));
                                break;
                            case 4:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1), adjacent.get(2), adjacent.get(3));
                                break;
                        }
                    }
                    else {
                        continue;
                    }
                }
            }

            if(undetermined == 0) {
                creating = false;
            }
            topval--;
            undetermined = 0;
            for(int i = SIZE-1; i > 0; i--) {
                for(int j = SIZE-1; j > 0; j--) {
                    if(elevations[i][j] < topval - 5) {
                        ArrayList<Integer> adjacent = new ArrayList<Integer>();
                        if(i > 0) {
                            adjacent.add(elevations[i-1][j]);
                        }
                        if(i < SIZE-1) {
                            adjacent.add(elevations[i+1][j]);
                        }
                        if(j > 0) {
                            adjacent.add(elevations[i][j-1]);
                        }
                        if(j < SIZE-1) {
                            adjacent.add(elevations[i][j+1]);
                        }
                        for(int k = adjacent.size()-1; k > -1; k--) {
                            if(adjacent.get(k) < topval) {
                                adjacent.remove(k);
                            }
                        }
                        switch(adjacent.size()) {
                            case 0:
                                undetermined++;
                                break;
                            case 1:
                                elevations[i][j] = picknumber(adjacent.get(0));
                                break;
                            case 2:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1));
                                break;
                            case 3:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1), adjacent.get(2));
                                break;
                            case 4:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1), adjacent.get(2), adjacent.get(3));
                                break;
                        }
                    }
                    else {
                        continue;
                    }
                }
            }
            if(undetermined == 0) {
                creating = false;
            }
            topval--;
            undetermined = 0;
            for(int i = 0; i < SIZE; i++) {
                for(int j = SIZE-1; j > 0; j--) {
                    if(elevations[i][j] < topval - 5) {
                        ArrayList<Integer> adjacent = new ArrayList<Integer>();
                        if(i > 0) {
                            adjacent.add(elevations[i-1][j]);
                        }
                        if(i < SIZE-1) {
                            adjacent.add(elevations[i+1][j]);
                        }
                        if(j > 0) {
                            adjacent.add(elevations[i][j-1]);
                        }
                        if(j < SIZE-1) {
                            adjacent.add(elevations[i][j+1]);
                        }
                        for(int k = adjacent.size()-1; k > -1; k--) {
                            if(adjacent.get(k) < topval) {
                                adjacent.remove(k);
                            }
                        }
                        switch(adjacent.size()) {
                            case 0:
                                undetermined++;
                                break;
                            case 1:
                                elevations[i][j] = picknumber(adjacent.get(0));
                                break;
                            case 2:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1));
                                break;
                            case 3:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1), adjacent.get(2));
                                break;
                            case 4:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1), adjacent.get(2), adjacent.get(3));
                                break;
                        }
                    }
                    else {
                        continue;
                    }
                }
            }
            if(undetermined == 0) {
                creating = false;
            }
            topval--;
            undetermined = 0;
            for(int i = SIZE-1; i > 0; i--) {
                for(int j = 0; j < SIZE; j++) {
                    if(elevations[i][j] < topval - 5) {
                        ArrayList<Integer> adjacent = new ArrayList<Integer>();
                        if(i > 0) {
                            adjacent.add(elevations[i-1][j]);
                        }
                        if(i < SIZE-1) {
                            adjacent.add(elevations[i+1][j]);
                        }
                        if(j > 0) {
                            adjacent.add(elevations[i][j-1]);
                        }
                        if(j < SIZE-1) {
                            adjacent.add(elevations[i][j+1]);
                        }
                        for(int k = adjacent.size()-1; k > -1; k--) {
                            if(adjacent.get(k) < topval) {
                                adjacent.remove(k);
                            }
                        }
                        switch(adjacent.size()) {
                            case 0:
                                undetermined++;
                                break;
                            case 1:
                                elevations[i][j] = picknumber(adjacent.get(0));
                                break;
                            case 2:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1));
                                break;
                            case 3:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1), adjacent.get(2));
                                break;
                            case 4:
                                elevations[i][j] = picknumber(adjacent.get(0), adjacent.get(1), adjacent.get(2), adjacent.get(3));
                                break;
                        }
                    }
                    else {
                        continue;
                    }
                }
            }
            System.out.println("determining " + undetermined + " points... (" + (int)(((double)SIZE*SIZE-undetermined)/(SIZE*SIZE)*100) + "%) Looking for value: " + (topval-1));
            if(undetermined == 0) {
                creating = false;
            }
            topval--;
            pnl.importList(elevations);
        }
    }

    public static void makeBiomes(MapPanel pnl) {
        char[][] biomes = new char[SIZE][SIZE];
        System.out.println("Creating biomes...");
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                biomes[i][j] = 'n';
            }
        }
        System.out.println("Adding peaks for random biomes...");
        for(int i = 0; i < 15; i++) {
            int x = (int)(Math.random()*SIZE);
            int y = (int)(Math.random()*SIZE);
            biomes[x][y] = pickbiome();
        }

        boolean creating = true;
        while(creating) {
            int undetermined = 0;
            for(int i = 0; i < SIZE; i++) {
                for(int j = 0; j < SIZE; j++) {
                    if(biomes[i][j] != 'n') {
                        if(i > 0 && biomes[i-1][j] == 'n') {
                            biomes[i-1][j] = pickbiome(biomes[i][j]);
                        }
                        if(i < SIZE-1 && biomes[i+1][j] == 'n') {
                            biomes[i+1][j] = pickbiome(biomes[i][j]);
                        }
                        if(j > 0 && biomes[i][j-1] == 'n') {
                            biomes[i][j-1] = pickbiome(biomes[i][j]);
                        }
                        if(j < SIZE-1 && biomes[i][j+1] == 'n') {
                            biomes[i][j+1] = pickbiome(biomes[i][j]);
                        }
                    }
                    else {
                        undetermined++;
                        continue;
                    }
                }
            }
            if(undetermined == 0) {
                creating = false;
            }
            undetermined = 0;
            for(int i = SIZE-1; i > 0; i--) {
                for(int j = SIZE-1; j > 0; j--) {
                    if(biomes[i][j] != 'n') {
                        if(i > 0 && biomes[i-1][j] == 'n') {
                            biomes[i-1][j] = pickbiome(biomes[i][j]);
                        }
                        if(i < SIZE-1 && biomes[i+1][j] == 'n') {
                            biomes[i+1][j] = pickbiome(biomes[i][j]);
                        }
                        if(j > 0 && biomes[i][j-1] == 'n') {
                            biomes[i][j-1] = pickbiome(biomes[i][j]);
                        }
                        if(j < SIZE-1 && biomes[i][j+1] == 'n') {
                            biomes[i][j+1] = pickbiome(biomes[i][j]);
                        }
                    }
                    else {
                        undetermined++;
                        continue;
                    }
                }
            }
            if(undetermined == 0) {
                creating = false;
            }
            undetermined = 0;
            for(int i = 0; i < SIZE; i++) {
                for(int j = SIZE-1; j > 0; j--) {
                    if(biomes[i][j] != 'n') {
                        if(i > 0 && biomes[i-1][j] == 'n') {
                            biomes[i-1][j] = pickbiome(biomes[i][j]);
                        }
                        if(i < SIZE-1 && biomes[i+1][j] == 'n') {
                            biomes[i+1][j] = pickbiome(biomes[i][j]);
                        }
                        if(j > 0 && biomes[i][j-1] == 'n') {
                            biomes[i][j-1] = pickbiome(biomes[i][j]);
                        }
                        if(j < SIZE-1 && biomes[i][j+1] == 'n') {
                            biomes[i][j+1] = pickbiome(biomes[i][j]);
                        }
                    }
                    else {
                        undetermined++;
                        continue;
                    }
                }
            }
            if(undetermined == 0) {
                creating = false;
            }
            undetermined = 0;
            for(int i = SIZE-1; i > 0; i--) {
                for(int j = 0; j < SIZE; j++) {
                    if(biomes[i][j] != 'n') {
                        if(i > 0 && biomes[i-1][j] == 'n') {
                            biomes[i-1][j] = pickbiome(biomes[i][j]);
                        }
                        if(i < SIZE-1 && biomes[i+1][j] == 'n') {
                            biomes[i+1][j] = pickbiome(biomes[i][j]);
                        }
                        if(j > 0 && biomes[i][j-1] == 'n') {
                            biomes[i][j-1] = pickbiome(biomes[i][j]);
                        }
                        if(j < SIZE-1 && biomes[i][j+1] == 'n') {
                            biomes[i][j+1] = pickbiome(biomes[i][j]);
                        }
                    }
                    else {
                        undetermined++;
                        continue;
                    }
                }
            }
            System.out.println("determining " + undetermined + " points... (" + (int)(((double)SIZE*SIZE-undetermined)/(SIZE*SIZE)*100) + "%)");
            if(undetermined == 0) {
                creating = false;
            }
            pnl.importBiomes(biomes);
        }
        pnl.importBiomes(biomes);
    }

    public static int picknumber(int a) {
        if(a == 50) {
            return a-1;
        }
        else if(a > 100 || a < -50) {
            if(Math.random()*5 < 3.0) {
                return a;
            }
        }
        else if(a > -20 && a < 0) {
            if(Math.random()*5 < 1) {
                return a;
            }
        }
        else if (a == 0) {
            return a-1;
        }
        else {
            if(Math.random()*5 < 2.0) {
                return a;
            }
        }
        return a-1;
    }

    public static int picknumber(int a, int b) {
        return(picknumber((a + b) / 2));
    }
    public static int picknumber(int a, int b, int c) {
        return(picknumber((a + b + c) / 3));
    }
    public static int picknumber(int a, int b, int c, int d) {
        return(picknumber((a + b + c + d) / 4));
    }

    public static char pickbiome() {
        char[] biomepick = {'d', 'f', 'p', 't', 'j', 's'};
        return biomepick[(int)(Math.random() * 6)];
    }
    public static char pickbiome(char c) {
        if(Math.random() < 0.5) {
            return c;
        }
        return 'n';
    }

    public static void printlist(int[][] ints) {
        for(int[] i : ints) {
            System.out.println(Arrays.toString(i));
        }
    }

}