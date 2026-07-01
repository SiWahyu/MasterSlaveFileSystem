package com.masterslave;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.masterslave.ui.slave.SlaveFrame;

import javax.swing.SwingUtilities;

public class SlaveApplication {

    public static void main(String[] args) {

        FlatIntelliJLaf.setup();

        SwingUtilities.invokeLater(SlaveFrame::new);

    }

}