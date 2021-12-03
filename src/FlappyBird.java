/**This program creates a flappy bird game and utilizes the JFrame ADT.
 * The player taps the screen if they want the bird to go stay up in the air. The columns
 * are also moving so the bird can go past them and give the player a point to their score.
 *
 */

import javax.swing.*;
//import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener {

    public static FlappyBird flappyBird;
    public final int Width = 800, Height = 800;
    public Renderer renderer;
    public Rectangle bird;
    public int ticks, yMotion, score;
    public boolean gameOver, started;
    public ArrayList<Rectangle> columns;
    public Random rand;

    public FlappyBird () {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();


        jframe.add(renderer);
        jframe.setTitle("Flappy Bird");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(Width, Height);
        jframe.addMouseListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);

        bird = new Rectangle(Width/2 - 10, Height/2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }
    public void repaint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0,0, Width, Height);

        g.setColor(Color.orange);
        g.fillRect(0, Height - 120, Width, 120);

        g.setColor(Color.green);
        g.fillRect(0, Height - 120, Width, 20);

        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : columns) {
            paintColumn(g,column);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));

        if (!started) {
            g.drawString("Click to Start!", 75, Height/2 - 50);
        }
        if (gameOver) {
            g.drawString("Game Over!", 100, Height/2 - 50);
        }
        if (!gameOver && started) {
            g.drawString(String.valueOf(score), Width/2 - 25, 100);
        }

    }
    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start) {
            columns.add(new Rectangle(Width + width + columns.size() * 300, Height - height - 120, width, height));
            columns.add(new Rectangle(Width + width + (columns.size() - 1) * 300, 0, width, Height - height - space));
        }
        else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, Height - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, Height - height - space ));
        }
    }


    public void paintColumn (Graphics g, Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump() {
        if (gameOver){

            bird = new Rectangle(Width/2 - 10, Height/2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            gameOver = false;
        }
        if (!started){
            started = true;
        }
        else if (!gameOver) {
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }
    public static void main(String []args){

        flappyBird = new FlappyBird();

    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 10;
        ticks++;
        if (started) {


            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);

                if (column.x + column.width < 0) {
                    columns.remove(column);
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle column : columns) {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10 ){
                    score++;
                }
                if (column.intersects(bird)) {
                    gameOver = true;

                    bird.x = column.x - bird.width;
                }
            }
            if (bird.y > Height - 120 || bird.y < 0) {
                gameOver = true;
            }
            if (bird.y + yMotion >= Height - 120){
                bird.y = Height - 120 - bird.height;
            }
        }
        renderer.repaint();
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
