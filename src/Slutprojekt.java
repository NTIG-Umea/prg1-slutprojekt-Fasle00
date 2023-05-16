import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.Arrays;

public class Slutprojekt extends Canvas implements Runnable{
    private BufferStrategy bs;
    private Boolean running = false;
    private Thread thread;

    private final int WIDTH = 1600;
    private final int HEIGHT = 900;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static int[][] grid = new int[COLUMNS][ROWS];
    private final int TOKENWIDTH = 150;
    private final int TOKENHEIGHT = 80;
    private static int turn = 0;
    private int pos = 0;
    private Boolean on = false;
    private int ending = 0;
    private int currentH = 0;
    private int endY = 0;

    public Slutprojekt(){
        setSize(WIDTH,HEIGHT);
        JFrame frame = new JFrame();
        frame.add(this);
        this.addMouseListener(new MyMouseListner());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        for (int i = 0; i < COLUMNS; i++){
            for (int j = 0; j < ROWS; j++){
                grid[i][j] = 0;
            }
        }
        Slutprojekt slutprojekt = new Slutprojekt();
        slutprojekt.start();
    }
    
    public void draw(Graphics g){
        g.clearRect(0,0,WIDTH,HEIGHT);
        drawGrid(g);
        if (on){
            if (turn % 2 == 0){
                g.setColor(Color.RED);
            }else {
                g.setColor(Color.YELLOW);
            }
            g.fillOval(pos * TOKENWIDTH + 200, currentH, TOKENWIDTH,TOKENHEIGHT);
        }
        if (isWinning){
            g.setColor(Color.GREEN);
            g.drawLine((int)((winX1 + 0.5) * TOKENWIDTH) + 200,(int)((winY1 + 0.5) * TOKENHEIGHT) + 200,(int)((winX2 + 0.5) * TOKENWIDTH) + 200,(int)((winY2 + 0.5) * TOKENHEIGHT) + 200);
        }
    }
    
    public void drawGrid(Graphics g){
        Color black = Color.BLACK;
        Color white = Color.WHITE;
        Color red = Color.RED;
        Color yellow = Color.YELLOW;
        g.setColor(black);
        g.fillRect(190,190,1070,500);
        for (int y = 0; y < ROWS; y++){
            for (int x = 0; x < COLUMNS; x++){
                if (grid[x][y] == 1){
                    g.setColor(red);
                } else if (grid[x][y] == 2) {
                    g.setColor(yellow);
                } else {
                    g.setColor(white);
                }
                g.fillOval(x * TOKENWIDTH + 200,y * TOKENHEIGHT + 200, TOKENWIDTH, TOKENHEIGHT);
            }
        }
    }
    
    private void update() {
        if (on){
            int h;
            if (currentH == 0){
                h = 5;
                for (int i = 5; i >= 0; i--){
                    if (grid[pos][i] >= 1){
                        h--;
                    }
                }
                ending = (h) * TOKENHEIGHT + 200;
                currentH = 150;
            }
            if (currentH < ending){
                currentH += 15;
            } else {
                if (turn % 2 == 0){
                    grid[pos][endY] = 1;
                } else {
                    grid[pos][endY] = 2;
                }
                currentH = 0;
                on = false;
                isWinning = checkWin();
            }
        }
    }
    private static int winX1;
    private static int winX2;
    private static int winY1;
    private static int winY2;
    private static Boolean isWinning = false;
    public static Boolean checkWin(){
        int player = turn % 2 + 1;
        for (int x = COLUMNS - 1; x >= 0; x--){
            for (int y = ROWS - 1; y >= 0; y--){
                Boolean win = false;
                try {
                    if (grid[x][y] == player && grid[x-1][y] == player && grid[x-2][y] == player && grid[x-3][y] == player){
                        winX1 = x;
                        winX2 = x-3;
                        winY1 = winY2 = y;
                        win = true;
                    }
                }catch (Exception e) { win = false; }

                if (win) {
                    System.out.println("x1: " + x + " y1: " + y);
                    System.out.println("player: " + player);
                    System.out.println(grid[x][y]);
                    System.out.println(grid[x-1][y]);
                    System.out.println(grid[x-2][y]);
                    System.out.println(grid[x-3][y]);
                    return true;
                }

                try {
                    if (grid[x][y] == player && grid[x][y-1] == player && grid[x][y-2] == player && grid[x][y-3] == player){
                        winY1 = y;
                        winY2 = y-3;
                        winX1 = winX2 = x;
                        win = true;
                    }
                } catch (Exception e) { win = false; }

                if (win) {
                    System.out.println("x1: " + x + " y1: " + y);
                    System.out.println("player: " + player);
                    System.out.println(grid[x][y]);
                    System.out.println(grid[x][y-1]);
                    System.out.println(grid[x][y-2]);
                    System.out.println(grid[x][y-3]);
                    return true;
                }

                try {
                    if (grid[x][y] == player && grid[x-1][y-1] == player && grid[x-2][y-2] == player && grid[x-3][y-3] == player){
                        winX1 = x;
                        winX2 = x - 3;
                        winY1 = y;
                        winY2 = y - 3;
                        win = true;
                    }
                } catch (Exception e) { win = false; }

                if (win) {
                    System.out.println("x1: " + x + " y1: " + y);
                    System.out.println("player: " + player);
                    System.out.println(grid[x][y]);
                    System.out.println(grid[x-1][y-1]);
                    System.out.println(grid[x-2][y-2]);
                    System.out.println(grid[x-3][y-3]);
                    return true;
                }

                try {
                    if (grid[x-3][y] == player && grid[x-2][y-1] == player && grid[x-1][y-2] == player && grid[x][y-3] == player){
                        winX1 = x - 3;
                        winX2 = x;
                        winY1 = y;
                        winY2 = y - 3;
                        win = true;
                    }
                }catch (Exception e) { win = false; }

                if (win) {
                    System.out.println("x1: " + x + " y1: " + y);
                    System.out.println("player: " + player);
                    System.out.println(grid[x-3][y]);
                    System.out.println(grid[x-2][y-1]);
                    System.out.println(grid[x-1][y-2]);
                    System.out.println(grid[x][y-3]);
                    return true;
                }
            }
        }
        return false;
    }

    public class MyMouseListner implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!on && !isWinning){
                if (e.getX() >= 200 && e.getX() < 1250){
                    pos = (int)((e.getX() - 200) / TOKENWIDTH);
                    System.out.println(pos);
                    for (int i = 5; i >= 0; i--){
                        if (grid[pos][i] >= 1){
                            if (i == 0){
                                return;
                            }
                        }else {
                            System.out.println(turn);
                            if (turn % 2 == 0){
                                System.out.println("i= " + i + "\npos= " + pos);
                                endY = i;
                                turn++;
                                on = true;
                                return;
                            }else if (turn % 2 == 1){
                                System.out.println("i= " + i + "\npos= " + pos);
                                endY = i;
                                on = true;
                                turn++;
                                return;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        @Override
        public void mouseEntered(MouseEvent e) {

        }
        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    public void render() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Rita ut den nya bilden
        draw(g);

        g.dispose();
        bs.show();
    }
    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        double ns = 1000000000.0 / 25.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med updaterad data
                render();
                delta--;
            }
        }
        stop();
    }
}
