import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
// import java.awt.Graphics;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.event.KeyAdapter;
// import java.awt.event.KeyEvent;

// import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT/UNIT_SIZE);
    int DELAY = 120;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    JButton restartButton;
    
    
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        restartButton = new JButton("Restart Game");
        restartButton.addActionListener(this);
        add(restartButton);
        startGame();
    }

    public void startGame(){
        bodyParts = 6;
        applesEaten = 0;
        DELAY = 120;
        direction = 'R';
        running = true;
        x[0] = SCREEN_WIDTH / 2;
        y[0] = SCREEN_HEIGHT / 2;
        for (int i = 1; i < bodyParts; i++) {
            x[i] = x[0] - i * UNIT_SIZE;
            y[i] = y[0];
        }
        timer = new Timer(DELAY, this);
        timer.start();
        // bodyParts = 6;
        // applesEaten = 0;
        // direction = 'R';
        // newApple();
        // running = true;
        // timer = new Timer(DELAY, this);
        // restartButton.setVisible(false);
        // timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        if(running){

            // Draw Lines
            for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.setColor(Color.WHITE);
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); 
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); 
    
            };
            // Apple color
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
    
            for(int i=0; i<bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.GREEN);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    // Snake back color
                    // g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            
        }
        else{

            // GameOver
            gameOver(g);

        }

    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i=bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if( (x[0] == appleX) && (y[0] == appleY) ){
            bodyParts++;
            applesEaten++;
            newApple();
            if(DELAY>30){
                DELAY -= 5;
            }else{
                
            }
            timer.setDelay(DELAY);
        }
    }
    public void checkCollisions(){
        // Head collides with body
        for(int i=bodyParts; i>0; i--){
            if( (x[0] == x[i]) && (y[0] == y[i]) ){
                running = false;
            }
        };
        // head collides with left side
        if(x[0]<0){
            running = false;
        }
        // head collides with right side
        if(x[0]>SCREEN_WIDTH){
            running = false;
        }
        // head collides with top side
        if(y[0]<0){
            running = false;
        }
        // head collides with bottom side
        if(y[0]>SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        // Game Over TEXT
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        // View Score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT/4);

        restartButton.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == restartButton){
            restartButton.setVisible(false);
            startGame();
        }
        else if(running){
            restartButton.setVisible(false);
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            // Add keybord controller
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
