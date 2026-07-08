package com.masterslave;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.masterslave.ui.slave.SlaveFrame;

import javax.swing.SwingUtilities;

public class SlaveApplication {

    public static void main(String[] args) {

        com.formdev.flatlaf.themes.FlatMacLightLaf.setup();
        javax.swing.UIManager.put("Button.arc", 15);
        javax.swing.UIManager.put("Component.arc", 15);
        javax.swing.UIManager.put("TextComponent.arc", 15);

        SwingUtilities.invokeLater(SlaveFrame::new);

    }

}