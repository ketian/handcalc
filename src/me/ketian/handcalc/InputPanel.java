package me.ketian.handcalc;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Ketian on 11/8/14.
 */
public class InputPanel extends JPanel {
    public static final int ROW = 28;
    public static final int COL = 28;
    public static final int SCALE = 5;
    public static final int BLACK = 255;
    public static final int GRAY = 127;
    public static final int LIGHT_GRAY = 63;
    public static final float SIGMA = 1;

    private int[][] data = new int[ROW*SCALE][COL*SCALE];
    private int[][] points = new int[ROW*COL*SCALE*SCALE+10][2];
    private int totalPoints;
    private float[][] convData = new float[ROW][COL];
    private float[][] gaussian = new float[SCALE][SCALE];
    private Timer refreshTimer = new Timer(50, new TimerListener());

    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

    private void init() {
        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.BLUE, 2));
        setPreferredSize(new Dimension(COL*SCALE, ROW*SCALE));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX(), y = e.getY();
                points[totalPoints][0] = x;
                points[totalPoints][1] = y;
                ++totalPoints;

                refreshTimer.start();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                refreshTimer.stop();
                repaint();
                totalPoints = 0;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX(), y = e.getY();

                if (totalPoints > 0)
                    if (points[totalPoints-1][0] == x && points[totalPoints-1][1] == y)
                        return;

                points[totalPoints][0] = x;
                points[totalPoints][1] = y;
                ++totalPoints;
            }
        });

        double mu = (double)(SCALE-1)/2.0, sum = 0;
        for (int i=0; i<SCALE; ++i)
            for (int j=0; j<SCALE; ++j) {
                gaussian[i][j] = (float)Math.exp( -( (i-mu)*(i-mu) + (j-mu)*(j-mu) ) / 2.0 / SIGMA );
                sum += gaussian[i][j];
            }
        for (int i=0; i<SCALE; ++i)
            for (int j=0; j<SCALE; ++j)
                gaussian[i][j] /= sum;
    }

    public InputPanel() {
        init();
    }

    public InputPanel(GridLayout layout) {
        super(layout);
        init();
    }

    private void refreshData(int x, int y) {
        for (int dx = -2 * SCALE; dx < 2 * SCALE; ++dx) {
            if (x + dx < 0 || x + dx >= ROW * SCALE) continue;
            for (int dy = -2 * SCALE; dy < 2 * SCALE; ++dy) {
                if (y + dy < 0 || y + dy >= COL * SCALE) continue;
                switch (Math.round((int) Math.sqrt(dx * dx + dy * dy) / SCALE)) {
                    case 0:
                        data[x + dx][y + dy] |= BLACK;
                        break;
                    case 1:
                        if (dx > 0 && dy > 0) data[x + dx][y + dy] |= LIGHT_GRAY;
                }
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // System.out.println("totalPoints = "+totalPoints);
        for (int i=0; i<totalPoints; ++i) {
            refreshData(points[i][0], points[i][1]);
            if (i<totalPoints-1) {
                float x = points[i][0], y = points[i][1];
                float dx = points[i+1][0]-points[i][0], dy = points[i+1][1]-points[i][1];
                if (Math.abs(dx)<Math.abs(dy)) {
                    dx /= Math.abs(dy);
                    dy /= Math.abs(dy);
                } else {
                        dy /= Math.abs(dx);
                        dx /= Math.abs(dx);
                }

                /*
                System.out.print("dx, dy = ");
                System.out.print(dx);
                System.out.print(", ");
                System.out.println(dy);

                System.out.print(x);
                System.out.print(", ");
                System.out.println(y);

                System.out.print(points[i+1][0]);
                System.out.print(", ");
                System.out.println(points[i+1][1]);

                System.out.print((points[i+1][0]-x)/dx);
                System.out.print(", ");
                System.out.println((points[i+1][1]-y)/dy);
                */

                x += dx; y += dy;
                while ( ( Math.abs(dx)<1e-6 || (points[i+1][0]-x)/dx>=-1e-6 ) &&
                        ( Math.abs(dy)<1e-6 || (points[i+1][1]-y)/dy>=-1e-6 ) ) {
                    refreshData(Math.round(x), Math.round(y));
                    x += dx; y += dy;
                }
            }
        }

        if (totalPoints > 1)
        {
            points[0][0] = points[totalPoints-1][0];
            points[0][1] = points[totalPoints-1][1];

            totalPoints = 1;
        }

        for (int i=0; i<ROW*SCALE; ++i)
            for (int j=0; j<COL*SCALE; ++j) {
                if (data[i][j] == 0) continue;
                if (data[i][j] == BLACK)
                    g.setColor(Color.BLACK);
                else if (data[i][j] == GRAY)
                    g.setColor(Color.GRAY);
                else if (data[i][j] == LIGHT_GRAY)
                    g.setColor(Color.LIGHT_GRAY);
                g.drawLine(i, j, i, j);
            }

    }

    public void resetData() {
        for (int i=0; i<ROW*SCALE; ++i)
            for (int j=0; j<COL*SCALE; ++j)
                data[i][j] = 0;
        totalPoints = 0;
        repaint();
    }

    public float[][] getConvData() {
        for (int i=0; i<ROW; ++i)
            for (int j=0; j<COL; ++j)
            {
                int x = i*SCALE, y = j*SCALE;
                for (int dx=0; dx<SCALE; ++dx)
                    for (int dy=0; dy<SCALE; ++dy)
                        if (dx==0 && dy==0) convData[i][j] = data[x][y]*gaussian[dx][dy];
                        else convData[i][j] += data[x+dx][y+dy]*gaussian[dx][dy];
            }
        return convData;
    }

}
