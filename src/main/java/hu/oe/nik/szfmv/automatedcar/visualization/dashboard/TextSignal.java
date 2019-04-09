package hu.oe.nik.szfmv.automatedcar.visualization.dashboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard visual component representing a general signal containing a text.
 * The component draws a rectangle to the given position.
 * The text content can be set to a given value.
 * The background color is white when switched off, green when switched on.
 */
public class TextSignal extends JPanel {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Color COLOR_TXT_BORDER = Color.black;
    private static final Color COLOR_OFF = Color.white;
    private static final Color COLOR_ON = Color.green;
    private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 13);

    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
    private int textXPos;
    private Color currentColor = COLOR_OFF;

    public TextSignal(int x, int y, int width, int height, String text, int textXPos) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.textXPos = textXPos;
        setBounds(x, y, width, height);
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public void setSwitchedOn(boolean switchedOn) {
        currentColor = switchedOn ? COLOR_ON : COLOR_OFF;
        // TODO Folyamatosan meghivodik
        //LOGGER.debug("setSwitchedOn: " + switchedOn + " (" + text + ")");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(currentColor);
        g.fillRect(0, 0, width, height);
        g.setColor(COLOR_TXT_BORDER);
        g.drawRect(0, 0, width - 1, height - 1);
        g.drawRect(1, 1, width - 3, height - 3);
        g.setFont(TEXT_FONT);
        g.drawString(text, textXPos, 18);
    }
}
