import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import java.util.ArrayList;
import java.util.Random;

// This class is responsible for run the program
// it will creates the applet and panel objects
public class DoublePendulum extends JApplet {

    public static void main(String s[]) {
        JFrame frame = new JFrame();
        frame.setTitle("Double Pendulum");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet applet = new DoublePendulum();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void init() {
        JPanel panel = new PanelDoublePendulum();
        getContentPane().add(panel);
    }
}

// This is the panel class that controls the redrawing process 
class PanelDoublePendulum extends JPanel implements Runnable {

    // wrapping math class
    private static final double PI = Math.PI;
    private static double sin(double a) {
        return Math.sin(a);
    }
    private static double cos(double a) {
        return Math.cos(a);
    }

    private static final int FW = 800;
    private static final int FH = 400;
    private static final int INIT_X = FW/2;
    private static final int INIT_Y = FH/2;
    private int frameSpeed = 20;

    private double g = 1;
    private double l1 = 100;
    private double l2 = 100;
    private double m1 = 20;
    private double m2 = 20;
    private double t1 = PI/2;
    private double t2 = PI/2;

    private double t1V = 0;
    private double t2V = 0;

    private int x1 = 0;
    private int y1 = 0;

    private int x2 = 0;
    private int y2 = 0;
    

    Thread mythread;
    
    public PanelDoublePendulum() {
        setPreferredSize(new Dimension(FW, FH));
        this.setBackground(Color.WHITE);

        this.mythread = new Thread(this);
        mythread.start();
    }

    @Override
    public void run() {
        while (true) {
        	try{
        		Thread.sleep(frameSpeed);
        	}
            catch(InterruptedException e){}

            this.calculateA();

            this.updateXY();
        	repaint();
        }  
    }

    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g2 = (Graphics2D) gr;

        g2.drawLine(INIT_X, INIT_Y, x1, y1);
        drawCenteredCircle(g2, x1, y1, (int) m1);

        g2.drawLine(x1, y1, x2, y2);
        drawCenteredCircle(g2, x2, y2, (int) m2);
    }

    private void calculateA() {
        
        // equation of motion for theat1 acceleration
        double num1 = -g * (2 * m1 + m2) * sin(t1);
        double num2 = -m2 * g * sin(t1-2*t2);
        double num3 = -2*sin(t1-t2)*m2;
        double num4 = t2V*t2V*l2+t1V*t1V*l1*cos(t1-t2);
        double den = l1 * (2*m1+m2-m2*cos(2*t1-2*t2));
        double t1A = (num1 + num2 + num3*num4) / den;


        // equation of motion for theta2 acceleration
        num1 = 2 * sin(t1-t2);
        num2 = (t1V*t1V*l1*(m1+m2));
        num3 = g * (m1 + m2) * cos(t1);
        num4 = t2V*t2V*l2*m2*cos(t1-t2);
        den = l2 * (2*m1+m2-m2*cos(2*t1-2*t2));
        double t2A = (num1*(num2+num3+num4)) / den;

        
        // add acceleration.
        this.t1V += t1A;
        this.t2V += t2A;

        // add velocity
        this.t1 += this.t1V;
        this.t2 += this.t2V;
        
    }

    private void updateXY() {
        this.x1 = (int) (INIT_X + l1 * sin(t1));
        this.y1 = (int) (INIT_Y + l1 * cos(t1));

        this.x2 = (int) (this.x1 + l2 * sin(t2));
        this.y2 = (int) (this.y1 + l2 * cos(t2));
    }

    private static void drawCenteredCircle(Graphics2D gr, int x, int y, int r) {
        x = x - (r/2);
        y = y - (r/2);
        gr.fillOval(x, y, r, r);
    }

     
}
