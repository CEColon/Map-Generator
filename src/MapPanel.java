package src;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.KeyListener;
import org.json.simple.JSONObject;

public class MapPanel extends JPanel implements KeyListener {

    public int[][] ints;
    public char[][] biomes;
    public char[][] regions;
    public static final int SIZE = 4096;
    public boolean realmoverlay;
    public boolean biomeoverlay;
    public boolean elevationoverlay;
    private int x_offset;
    private int y_offset;

    public MapPanel() {
        setPreferredSize(new Dimension(800, 700));
        setBackground(new Color(0, 0, 0));
        ints = new int[SIZE][SIZE];
        biomes = new char[SIZE][SIZE];
        regions = new char[SIZE][SIZE];
        realmoverlay = true;
        biomeoverlay = false;
        elevationoverlay = false;
        x_offset = 0;
        y_offset = 0;
        setFocusable(true);
        addKeyListener(this);
        Timer t = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        t.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = x_offset; i < x_offset+512; i++) {
            for(int j = y_offset; j < y_offset+512; j++) {
                if(realmoverlay) {
                    if(regions[i][j] == 'r') {
                        g.setColor(new Color(96, 50, 0));
                    }
                    else if(ints[i][j] < -6) {
                        g.setColor(new Color(0, 0, 255));
                    }
                    else if(ints[i][j] >= -6 && ints[i][j] < 2) {
                        g.setColor(new Color(0, 128, 255));
                    }
                    else if(ints[i][j] >= 2 && ints[i][j] < 10) {
                        g.setColor(new Color(200, 200, 0));
                    }
                    else if(ints[i][j] >= 10 && ints[i][j] < 30) {
                        g.setColor(new Color(0, 130, 0));

                    }
                    else if(ints[i][j] >= 30 && ints[i][j] < 70) {
                        g.setColor(new Color(0, 200, 0));
                    }
                    else if(ints[i][j] >= 70 && ints[i][j] < 90) {
                        g.setColor(new Color(50, 225, 0));
                    }
                    else if(ints[i][j] >= 90) {
                        g.setColor(new Color(100, 100, 100));
                    }
                }
                else if(biomeoverlay) {
                    if(biomes[i][j] == 'd') {
                        g.setColor(new Color(255, 255, 0));
                    }
                    else if(biomes[i][j] == 'f') {
                        g.setColor(new Color(0, 130, 0));
                    }
                    else if(biomes[i][j] == 'j') {
                        g.setColor(new Color(0, 230, 0));
                    }
                    else if(biomes[i][j] == 's') {
                        g.setColor(new Color(100, 200, 0));
                    }
                    else if(biomes[i][j] == 't') {
                        g.setColor(new Color(0, 200, 200));
                    }
                    else if(biomes[i][j] == 'p'){
                        g.setColor(new Color(0, 170, 0));
                    }
                    else {
                        g.setColor(new Color(50, 50, 50));
                    }
                }
                else {
                    if(ints[i][j] < -305) {
                        g.setColor(new Color(0, 0, 0));
                    }
                    else if(ints[i][j] < -50 && ints[i][j] >= -305) {
                        g.setColor(new Color(0, 0, ints[i][j]+305));
                    }
                    else if(ints[i][j] >= -50 && ints[i][j] < 0) {
                        g.setColor(new Color(0, 255/50*(ints[i][j]+50), 255));
                    }
                    else if(ints[i][j] >= 0 && ints[i][j] < 50) {
                        g.setColor(new Color(0, 255, 255-(255/50*(ints[i][j]))));
                    }
                    else if(ints[i][j] >= 50 && ints[i][j] < 100) {
                        g.setColor(new Color(255/50*(ints[i][j]-50), 255, 0));
                    }
                    else if(ints[i][j] >= 100) {
                        g.setColor(new Color(255, 255-(255/50*(ints[i][j]-100)), 0));
                    }
                }
                g.fillRect(i-x_offset, j-y_offset, 1, 1);

            }
        }
        for(int i = 0; i < SIZE; i+=32) {
            for(int j = 0; j < SIZE; j+=32) {
                if(realmoverlay) {
                    //if()
                    if(ints[i][j] < -6) {
                        g.setColor(new Color(0, 0, 255));
                    }
                    else if(ints[i][j] >= -6 && ints[i][j] < 2) {
                        g.setColor(new Color(0, 128, 255));
                    }
                    else if(ints[i][j] >= 2 && ints[i][j] < 10) {
                        g.setColor(new Color(200, 200, 0));
                    }
                    else if(ints[i][j] >= 10 && ints[i][j] < 30) {
                        g.setColor(new Color(0, 130, 0));

                    }
                    else if(ints[i][j] >= 30 && ints[i][j] < 70) {
                        g.setColor(new Color(0, 200, 0));
                    }
                    else if(ints[i][j] >= 70 && ints[i][j] < 90) {
                        g.setColor(new Color(50, 225, 0));
                    }
                    else if(ints[i][j] >= 90) {
                        g.setColor(new Color(100, 100, 100));
                    }
                }
                else if(biomeoverlay) {
                    if(biomes[i][j] == 'd') {
                        g.setColor(new Color(255, 255, 0));
                    }
                    else if(biomes[i][j] == 'f') {
                        g.setColor(new Color(0, 130, 0));
                    }
                    else if(biomes[i][j] == 'j') {
                        g.setColor(new Color(0, 230, 0));
                    }
                    else if(biomes[i][j] == 's') {
                        g.setColor(new Color(100, 200, 0));
                    }
                    else if(biomes[i][j] == 't') {
                        g.setColor(new Color(0, 200, 200));
                    }
                    else if(biomes[i][j] == 'p'){
                        g.setColor(new Color(0, 170, 0));
                    }
                    else {
                        g.setColor(new Color(50, 50, 50));
                    }
                }
                else {
                    if(ints[i][j] < -305) {
                        g.setColor(new Color(0, 0, 0));
                    }
                    else if(ints[i][j] < -50 && ints[i][j] >= -305) {
                        g.setColor(new Color(0, 0, ints[i][j]+305));
                    }
                    else if(ints[i][j] >= -50 && ints[i][j] < 0) {
                        g.setColor(new Color(0, 255/50*(ints[i][j]+50), 255));
                    }
                    else if(ints[i][j] >= 0 && ints[i][j] < 50) {
                        g.setColor(new Color(0, 255, 255-(255/50*(ints[i][j]))));
                    }
                    else if(ints[i][j] >= 50 && ints[i][j] < 100) {
                        g.setColor(new Color(255/50*(ints[i][j]-50), 255, 0));
                    }
                    else if(ints[i][j] >= 100) {
                        g.setColor(new Color(255, 255-(255/50*(ints[i][j]-100)), 0));
                    }
                }
                g.fillRect(i/32+512, j/32, 1, 1);
            }
        }
        g.setColor(new Color(255, 0, 0));
        g.fillRect(x_offset/32+512, y_offset/32, 16, 16);
    }

    public void importList(int[][] intlist) {
        ints = intlist;
    }
    public void importBiomes(char[][] charlist) {
        biomes = charlist;
    }
    public void importRegions(char[][] charlist) {
        regions = charlist;
    }

    public void createRegions(int[][] intlist, ArrayList<Integer> xvals, ArrayList<Integer> yvals) {
        System.out.println("Determining bridge locations...");
        ArrayList<Integer> bridgeX = new ArrayList<Integer>();
        ArrayList<Integer> bridgeY = new ArrayList<Integer>();
        ArrayList<String> bridgeDir = new ArrayList<String>();
        for(int i = 0; i < xvals.size(); i++) {
            int xval = xvals.get(i);
            int yval = yvals.get(i);
            boolean running = true;
            int x = xval;
            int y = yval;
            String dir = "west";
            boolean requiresBridge = false;
            while(running) {
                if(x < SIZE/8) {
                    break;
                } else if(ints[y][x-1] > ints[y][x]) {
                    if(requiresBridge) {
                        bridgeX.add(xval);
                        bridgeY.add(yval);
                        bridgeDir.add(dir);
                        System.out.println("Island at " + xval + ", " + yval + " needs a bridge to the " + dir);
                    }
                    break;
                }
                if(ints[y][x] < 0) {
                    requiresBridge = true;
                }
                x--;
            }
            x = xval;
            dir = "east";
            requiresBridge = false;
            while(running) {
                if(x > SIZE/4*3 + SIZE/8) {
                    break;
                } else if(ints[y][x+1] > ints[y][x]) {
                    if(requiresBridge) {
                        bridgeX.add(xval);
                        bridgeY.add(yval);
                        bridgeDir.add(dir);
                        System.out.println("Island at " + xval + ", " + yval + " needs a bridge to the " + dir);
                    }
                    break;
                }
                if(ints[y][x] < 0) {
                    requiresBridge = true;
                }
                x++;
            }
            x = xval;
            dir = "north";
            requiresBridge = false;
            while(running) {
                if(y < SIZE/8) {
                    break;
                } else if(ints[y-1][x] > ints[y][x]) {
                    if(requiresBridge) {
                        bridgeX.add(xval);
                        bridgeY.add(yval);
                        bridgeDir.add(dir);
                        System.out.println("Island at " + xval + ", " + yval + " needs a bridge to the " + dir);
                    }
                    break;
                }
                if(ints[y][x] < 0) {
                    requiresBridge = true;
                }
                y--;
            }
            y = yval;
            dir = "south";
            requiresBridge = false;
            while(running) {
                if(y > SIZE/4*3 + SIZE/8) {
                    break;
                } else if(ints[y+1][x] > ints[y][x]) {
                    if(requiresBridge) {
                        bridgeX.add(xval);
                        bridgeY.add(yval);
                        bridgeDir.add(dir);
                        System.out.println("Island at " + xval + ", " + yval + " needs a bridge to the " + dir);
                    }
                    break;
                }
                if(ints[y][x] == 0) {
                    requiresBridge = true;
                }
                y++;
            }
            y = yval;
        }
        System.out.println("Setting up regions...");
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                if(intlist[i][j] > -1 && intlist[i][j] < 10) {
                    if(Math.random() < 0.01) {
                        regions[i][j] = 's';
                    }
                    else {
                        regions[i][j] = 'n';
                    }
                }
                else if(intlist[i][j] > 0) {
                    if(Math.random() < 0.1) {
                        regions[i][j] = 'e';
                    }
                    else {
                        regions[i][j] = 'n';
                    }
                }
                else {
                    regions[i][j] = 'n';
                }
            }
        }
        System.out.println("Done");
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            if(y_offset != 0) {
                y_offset-=32;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(x_offset != 0) {
                x_offset-=32;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            if(y_offset != SIZE-512) {
                y_offset+=32;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(x_offset != SIZE-512) {
                x_offset+=32;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_E) {
            elevationoverlay = true;
            realmoverlay = false;
            biomeoverlay = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_R) {
            elevationoverlay = false;
            realmoverlay = true;
            biomeoverlay = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_B) {
            elevationoverlay = false;
            realmoverlay = false;
            biomeoverlay = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_S) {
            try {
                File f = new File("map1.json");
                if(f.createNewFile()) {
                    System.out.println("Map File created");
                }
                else {
                    System.out.println("File already exists");
                }
                FileWriter w = new FileWriter(f);
                for(int i = 0; i < SIZE; i++) {
                    for(int j = 0; j < SIZE; j++) {
                        JSONObject o = new JSONObject();
                        o.put("x", "" + j);
                        o.put("y", "" + i);
                        o.put("elevation", "" + ints[i][j]);
                        o.put("biome", "" + biomes[i][j]);
                        o.put("region", "" + regions[i][j]);
                        w.write(o.toJSONString());
                        w.write("\n");
                    }
                    System.out.println("(" + (int)((double)i/4096*100) + "%)");
                }
                w.close();
                System.out.println("Done");
            }
            catch(Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    public void keyReleased(KeyEvent e) {
        //Unused
    }
    public void keyTyped(KeyEvent e) {
        //Unused
    }

}
