import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

public class Slutprojekt extends Canvas implements Runnable{
    private BufferStrategy bs;
    private Boolean running = false;
    private Thread thread;

    private final int WIDTH = 1500;
    private final int HEIGHT = 900;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static int[][] grid = new int[COLUMNS][ROWS];
    private static final int TOKENWIDTH = 150;
    private static final int TOKENHEIGHT = 90;
    private static int turn = 0;
    private static int pos = 0;
    private Boolean on = false;
    private int ending = 0;
    private int currentH = 95;
    private int mouseX = 500;
    private static final int XYOFFSET = 200;
    private int yellowWinCount = 0;
    private int redWinCount = 0;
    private Font myFont = new Font(null,1,20);

    public Slutprojekt(){
        setSize(WIDTH,HEIGHT);
        JFrame frame = new JFrame();
        frame.add(this);
        this.addMouseListener(new MyMouseListner());
        this.addMouseMotionListener(new MyMouseMotionListner());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        resetBoard();
        Slutprojekt slutprojekt = new Slutprojekt();
        slutprojekt.start();
    }
    
    public void draw(Graphics g){
        g.clearRect(0,0,WIDTH,HEIGHT);
        drawGrid(g);
        dropToken(g);
        g.setFont(myFont);

        drawScore(g);

        if (isWinning){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(5));
            g2.drawLine(winX1,winY1,winX2,winY2);
            g.setColor(new Color(122, 58, 0));
            g.fillRect(700,100,240,100);
            g.setColor(Color.BLACK);
            g.drawRect(700,100,240,100);
            if (turn % 2 == 0){
                g.drawString("Red player won",745,155);
            }else{
                g.drawString("Yellow player won",728,155);
            }
            g.drawString("Click here to play again",707,180);
        } else {
            hover(g);
        }
    }

    public void drawScore(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillOval(20,50,80,80);
        g.setColor(Color.RED);
        g.fillOval(1400,50,80,80);
        g.setColor(Color.BLACK);
        g.drawOval(20,50,80,80);
        g.drawOval(1400,50,80,80);
        g.drawString(String.valueOf(yellowWinCount),120,100);
        g.drawString(String.valueOf(redWinCount),1370 - (String.valueOf(redWinCount).length() - 1) * 8,100);
    }

    public void dropToken(Graphics g){
        if (on){
            if (turn % 2 == 0){
                g.setColor(Color.RED);
            }else {
                g.setColor(Color.YELLOW);
            }
            g.fillOval(pos * TOKENWIDTH + XYOFFSET, currentH, TOKENWIDTH,TOKENHEIGHT);
        }
    }

    public void hover(Graphics g){
        if (turn % 2 == 0){
            g.setColor(Color.YELLOW);
        }else {
            g.setColor(Color.RED);
        }
        g.fillOval(mouseX,95,TOKENWIDTH,TOKENHEIGHT);
        g.setColor(Color.BLACK);
        g.drawOval(mouseX,95,TOKENWIDTH,TOKENHEIGHT);
    }
    
    public void drawGrid(Graphics g){
        Color black = Color.BLACK;
        Color white = Color.WHITE;
        Color red = Color.RED;
        Color yellow = Color.YELLOW;
        g.setColor(black);
        g.fillRect(190,190,COLUMNS * TOKENWIDTH + 20,ROWS * TOKENHEIGHT + 20);
        for (int y = 0; y < ROWS; y++){
            for (int x = 0; x < COLUMNS; x++){
                if (grid[x][y] == 1){
                    g.setColor(red);
                } else if (grid[x][y] == 2) {
                    g.setColor(yellow);
                } else {
                    g.setColor(white);
                }
                g.fillOval(x * TOKENWIDTH + XYOFFSET,y * TOKENHEIGHT + XYOFFSET, TOKENWIDTH, TOKENHEIGHT);
            }
        }
    }
    
    private void update() {
        if (on){
            if (currentH == 95){
                int h = 5;
                for (int i = 5; i >= 0; i--){
                    if (grid[pos][i] >= 1){
                        h--;
                    }
                }
                ending = (h) * TOKENHEIGHT + XYOFFSET;
            }
            if (currentH < ending){
                currentH += 15;
            } else {
                int player = turn % 2;
                if (player == 0){
                    grid[pos][(ending-XYOFFSET)/TOKENHEIGHT] = 1;
                } else {
                    grid[pos][(ending-XYOFFSET)/TOKENHEIGHT] = 2;
                } // places token in grid
                currentH = 95;
                on = false;
                if (checkWin()){
                    if (player == 0) {
                        redWinCount++;
                    }else {
                        yellowWinCount++;
                    }
                    isWinning = true;
                }
            }
        }
    }
    private int winX1, winX2, winY1, winY2;
    private static Boolean isWinning = false;
    public Boolean checkWin(){
        int player = turn % 2 + 1;
        for (int x = 6; x >= 0; x--){
            for (int y = 5; y >= 0; y--){
                if (x > 2){
                    if (grid[x][y] == player && grid[x-1][y] == player && grid[x-2][y] == player && grid[x-3][y] == player){
                            System.out.println("vänster");
                            winX1 = (int)((x + 0.5) * TOKENWIDTH) + XYOFFSET;
                            winX2 = (int)((x - 2.5) * TOKENWIDTH) + XYOFFSET;
                            winY1 = winY2 = (int)((y + 0.5) * TOKENHEIGHT) + XYOFFSET;
                            return true;
                        } // vågrät
                    if (y > 2) {
                        if (grid[x][y] == player && grid[x - 1][y - 1] == player && grid[x - 2][y - 2] == player && grid[x - 3][y - 3] == player) {
                                System.out.println("vänster up");
                                winX1 = (int) ((x + 0.5) * TOKENWIDTH) + XYOFFSET;
                                winX2 = (int) ((x - 2.5) * TOKENWIDTH) + XYOFFSET;
                                winY1 = (int) ((y + 0.5) * TOKENHEIGHT) + XYOFFSET;
                                winY2 = (int) ((y - 2.5) * TOKENHEIGHT) + XYOFFSET;
                                return true;
                            } // sne up
                        if (grid[x][y] == player && grid[x][y - 1] == player && grid[x][y - 2] == player && grid[x][y - 3] == player) {
                                System.out.println("ner");
                                winY1 = (int)((y + 0.5) * TOKENHEIGHT) + XYOFFSET;
                                winY2 = (int)((y - 2.5) * TOKENHEIGHT) + XYOFFSET;
                                winX1 = winX2 = (int)((x + 0.5) * TOKENWIDTH) + XYOFFSET;
                                return true;
                            } // lodrät
                    } else {
                        if (grid[x][y] == player && grid[x - 1][y + 1] == player && grid[x - 2][y + 2] == player && grid[x - 3][y + 3] == player) {
                                System.out.println("vänster ner");
                                winX1 = (int) ((x - 2.5) * TOKENWIDTH) + XYOFFSET;
                                winX2 = (int) ((x + 0.5) * TOKENWIDTH) + XYOFFSET;
                                winY1 = (int) ((y + 3.5) * TOKENHEIGHT) + XYOFFSET;
                                winY2 = (int) ((y + 0.5) * TOKENHEIGHT) + XYOFFSET;
                                return true;
                            } // sne ner
                    }
                }else if (y > 2) {
                    if (grid[x][y] == player && grid[x][y - 1] == player && grid[x][y - 2] == player && grid[x][y - 3] == player) {
                        System.out.println("ner");
                        winY1 = (int)((y + 0.5) * TOKENHEIGHT) + XYOFFSET;
                        winY2 = (int)((y - 2.5) * TOKENHEIGHT) + XYOFFSET;
                        winX1 = winX2 = (int)((x + 0.5) * TOKENWIDTH) + XYOFFSET;
                        return true;
                    } // lodrät
                } // lodrät om x <= 2
            }
        }
        return false;
    }
    public static void resetBoard() {
        for (int i = 0; i < COLUMNS; i++){
            for (int j = 0; j < ROWS; j++){
                grid[i][j] = 0;
            }
        }
        isWinning = false;
    }

    public class MyMouseMotionListner implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (e.getX() >= XYOFFSET + (TOKENWIDTH / 2) && e.getX() <= 1250 - (TOKENWIDTH / 2)){
                mouseX = e.getX() - (TOKENWIDTH / 2);
            }
        }
    }
    public class MyMouseListner implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println(e.getButton());
            if (!isWinning){
                if (!on){
                    if (e.getX() >= XYOFFSET && e.getX() < 1250){
                        pos = (e.getX() - XYOFFSET) / TOKENWIDTH;
                        for (int i = 5; i >= 0; i--){
                            if (grid[pos][i] >= 1){
                                if (i == 0){
                                    return;
                                }
                            }else {
                                turn++;
                                on = true;
                                return;
                            }
                        }
                    }
                }
            }else if (e.getX() > 700 && e.getX() < 900 && e.getY() > 100 && e.getY() < XYOFFSET){
                resetBoard();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println(e.getButton());
            System.out.println(e.getX());
            System.out.println(e.getY());
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
