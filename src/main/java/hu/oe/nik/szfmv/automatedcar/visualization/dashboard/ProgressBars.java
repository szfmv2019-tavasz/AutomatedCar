package hu.oe.nik.szfmv.automatedcar.visualization.dashboard;

import javax.swing.*;

public class ProgressBars extends JProgressBar {

    private static final int MAXVALUE = 100;
    private static final int MINVALUE = 0;

    private int x;
    private int y;
    private int width;
    private int height;

    public ProgressBars(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setBounds(x, y, width, height);
        setStringPainted(true);
        setMaximum(MAXVALUE);
        setMinimum(MINVALUE);
    }


}
