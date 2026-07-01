package com.masterslave;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.masterslave.ui.master.MasterFrame;

import javax.swing.SwingUtilities;

public class MasterApplication {

    public static void main(String[] args) {

        FlatIntelliJLaf.setup();

        SwingUtilities.invokeLater(MasterFrame::new);

    }

}