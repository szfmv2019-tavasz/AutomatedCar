package hu.oe.nik.szfmv.automatedcar.visualization.dashboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard visual component representing a turn signal.
 * The component draws an arrow to the given position.
 * The direction of the arrow depends on whether the turning is to left or right.
 * The background color is gray when switched off, orange when switched on.
 *
 * The component has the following measures:
 * width = 30
 * height = 30
 */
public class TurnSignal extends JPanel {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private static final Color COLOR_BACKGROUND = new Color(0x888888);
    private static final Color COLOR_OFF = Color.white;
    private static final Color COLOR_ON = Color.orange;

    private int[] xCoords;
    private int[] yCoords;
    private Color currentColor = COLOR_OFF;

    public TurnSignal(int x, int y, boolean left) {
        setBounds(x, y, WIDTH, HEIGHT);
        if (left) {
            xCoords = new int[] { 0, 10, 10, 20, 20, 10, 10 };
            yCoords = new int[] { 14, 0, 10, 10, 18, 18, 28 };
        } else {
            xCoords = new int[] { 0, 10, 10, 20, 10, 10, 0 };
            yCoords = new int[] { 10, 10, 0, 14, 28, 18, 18 };
        }
    }

    public void setSwitchedOn(boolean switchedOn) {
        currentColor = switchedOn ? COLOR_ON : COLOR_OFF;
        // TODO Folyamatosan meghivodik
        //LOGGER.debug("TurnSignal.setSwitchedOn: " + switchedOn);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(COLOR_BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(currentColor);
        g.fillPolygon(xCoords, yCoords, 7);
    }
}
