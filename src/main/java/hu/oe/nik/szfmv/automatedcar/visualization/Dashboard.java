package hu.oe.nik.szfmv.automatedcar.visualization;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard shows the state of the ego car, thus helps in debugging.
 */
public class Dashboard extends JPanel {

    private final int width = 250;
    private final int height = 700;
    private final int backgroundColor = 0x888888;

    Gui parent;

    private TurnSignal leftTurnSignal;
    private TurnSignal rightTurnSignal;

    private Thread timer = new Thread() {
        int difference;

        public void run() {
            while (true) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }

            }
        }
    };

    /**
     * Initialize the dashboard
     */
    public Dashboard(Gui pt) {
        // Not using any layout manager, but fixed coordinates
        setLayout(null);
        setBackground(new Color(backgroundColor));
        setBounds(770, 0, width, height);

        parent = pt;

        leftTurnSignal = addTurnSignal(new Point(30, 150), false);
        rightTurnSignal = addTurnSignal(new Point(180, 150), true);

        timer.start();
    }

    private TurnSignal addTurnSignal(Point position, boolean isRightSignal) {
        TurnSignal turnSignal = new TurnSignal(position, isRightSignal);
        add(turnSignal);
        return turnSignal;
    }

    public class TurnSignal extends JPanel {
        private final int WIDTH=30;
        private final int HEIGHT=30;

        Color color;
        Point position;
        boolean isRightSignal;

        public void setColor(Color color) {
            this.color = color;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0x888888));
            g.fillOval(-10, -10, 50, 50);
            g.setColor(color);
            if (isRightSignal) {
                int[] x = new int[] { 0, 10, 10, 20, 10, 10, 0 };
                int[] y = new int[] { 10, 10, 0, 14, 28, 18, 18 };
                g.fillPolygon(x, y, 7);
            } else {
                int[] x = new int[] { 0, 10, 10, 20, 20, 10, 10 };
                int[] y = new int[] { 14, 0, 10, 10, 18, 18, 28 };
                g.fillPolygon(x, y, 7);
            }
        }

        public TurnSignal(Point position, boolean isRightArrow)
        {
            this.position=position;
            this.isRightSignal =isRightArrow;
            this.color=Color.black;
            this.setBounds(position.x, position.y, WIDTH, HEIGHT);
        }
    }
}