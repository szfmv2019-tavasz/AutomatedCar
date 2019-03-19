package hu.oe.nik.szfmv.automatedcar.visualization.dashboard;

import javax.swing.*;


public class DashboardText extends JLabel {

    private int x;
    private int y;
    private int width;
    private int height;

    public DashboardText(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setText(text);
        setBounds(x, y, width, height);
    }

}
