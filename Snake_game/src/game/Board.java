package game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Board extends JPanel implements ActionListener {
    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private int dots;
    private int apple_x;
    private int apple_y;
    private boolean inGame = true;
    private boolean leftDirection = false;
    private boolean upDirection = false;
    private boolean rightDirection = true;
    private boolean downDirection = false;

    private Timer timer;

    public Board() {
        initBoard();
    }

    private void initGame() 
    {
        dots = 3;
        for (int z=0;z<dots;z++) 
        {
            x[z]=50-z*DOT_SIZE;
            y[z]=50;
        }

        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void initBoard() 
    {
        addKeyListener(new TAdapter());
        setBackground(new Color(124, 252, 0)); 
        setFocusable(true);  

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        initGame();
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }
    private void drawGrass(Graphics g) 
    {
        g.setColor(new Color(34, 139, 34));  
        for (int i = 0; i < B_WIDTH; i += 10) 
        { 
            for (int j = 0; j < B_HEIGHT; j += 10) 
            {  
                g.drawLine(i, j, i + 3, j + 7);  
            }
        }
    }
    private void restartGame() 
    {
        inGame = true;
        dots = 3;
        for (int z = 0; z < dots; z++)
        {
            x[z] = 50 - z * DOT_SIZE;
            y[z] = 50;
        }
        locateApple();
        timer.start();
    }
    private void doDrawing(Graphics g) 
    {
        if(inGame) 
        {
            drawGrass(g);
            g.setColor(Color.RED);
            g.fillOval(apple_x, apple_y, DOT_SIZE, DOT_SIZE);
            for (int z = 0; z < dots; z++) {
                g.setColor(new Color(255, 0, 0));
                g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE); 
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }
    private void gameOver(Graphics g) {
        String msg = "Game Over! Press R to Restart";
        g.setColor(Color.WHITE);
        g.drawString(msg, B_WIDTH / 2 - 60, B_HEIGHT/2);
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) 
        {
            dots++;
            locateApple();
        }
    }
    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[z - 1];
            y[z] = y[z - 1];
        }
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if (z > 4 && x[0] == x[z] && y[0] == y[z]) {
                inGame = false; 
            }
        }
        if (y[0] >= B_HEIGHT) {
            y[0] = 0;  
        }

        if (y[0] < 0) {
            y[0] = B_HEIGHT - DOT_SIZE;
        }
        if (x[0] >= B_WIDTH) {
            x[0] = 0;  
        }
        if (x[0] < 0)
        {
            x[0] = B_WIDTH - DOT_SIZE;  
        }

    }
    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = r * DOT_SIZE;

        r = (int) (Math.random() * RAND_POS);
        apple_y = r * DOT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            //restart
            if (key == KeyEvent.VK_R && !inGame) {
                restartGame();
            }
            //wsad
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                if (!rightDirection) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
            }
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                if (!leftDirection) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
            }
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                if (!downDirection) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }

            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                if (!upDirection) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }
        }
    }

   
}
