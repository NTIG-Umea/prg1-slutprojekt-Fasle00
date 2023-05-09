import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

public class Slutprojekt extends Canvas implements Runnable{
    private BufferStrategy bs;
    private Boolean running = false;
    private Thread thread;

    private final int WIDTH = 1600;
    private final int HEIGHT = 900;
    private final int ROWS = 6;
    private final int COLUMNS = 7;
    private int[][] grid = new int[COLUMNS][ROWS];
    private final int TOKENWIDTH = 150;
    private final int TOKENHEIGHT = 80;
    private int turn = 0;
    private int pos = 0;
    private Boolean on = false;
    private int ending = 0;
    private int currentH = 0;

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
        Slutprojekt slutprojekt = new Slutprojekt();
        slutprojekt.start();
    }
    
    public void draw(Graphics g){
        g.clearRect(0,0,getWidth(),getHeight());
        drawGrid(g);
        if (on){
            if (turn % 2 == 0)
            g.fillOval(pos * TOKENWIDTH + 200, currentH, TOKENWIDTH,TOKENHEIGHT);
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
                ending = (h + 1) * TOKENHEIGHT + 200;
                currentH = 150;
            }
            if (currentH < ending){
                currentH += 10;
            } else {
                currentH = 0;
                on = false;
            }
        }
    }

    public class MyMouseListner implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
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
                            grid[pos][i] = 1;
                            turn++;
                            on = true;
                            return;
                        }else if (turn % 2 == 1){
                            System.out.println("i= " + i + "\npos= " + pos);
                            grid[pos][i] = 2;
                            on = true;
                            turn++;
                            return;
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
