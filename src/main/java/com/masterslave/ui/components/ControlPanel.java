package com.masterslave.ui.components;

import com.masterslave.util.UIConstants;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final JButton startButton;
    private final JButton stopButton;

    public ControlPanel() {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        setOpaque(false);

        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");

        startButton.setFocusable(false);
        stopButton.setFocusable(false);

        stopButton.setEnabled(false);

        startButton.setPreferredSize(new Dimension(130, 35));
        stopButton.setPreferredSize(new Dimension(130, 35));

        startButton.setFont(UIConstants.NORMAL_FONT);
        stopButton.setFont(UIConstants.NORMAL_FONT);

        startButton.setBackground(UIConstants.SUCCESS);
        startButton.setForeground(Color.WHITE);
        startButton.putClientProperty("JButton.buttonType", "roundRect");

        stopButton.setBackground(UIConstants.ERROR);
        stopButton.setForeground(Color.WHITE);
        stopButton.putClientProperty("JButton.buttonType", "roundRect");

        add(startButton);
        add(stopButton);

    }

    /**
     * Mengembalikan tombol Start Server.
     */
    public JButton getStartButton() {
        return startButton;
    }

    /**
     * Mengembalikan tombol Stop Server.
     */
    public JButton getStopButton() {
        return stopButton;
    }

    /**
     * Mengaktifkan atau menonaktifkan tombol sesuai status server.
     */
    public void setServerRunning(boolean running) {

        startButton.setEnabled(!running);

        stopButton.setEnabled(running);

    }

}