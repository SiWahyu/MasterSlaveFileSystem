package com.masterslave;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.masterslave.ui.master.MasterFrame;

import javax.swing.SwingUtilities;

public class MasterApplication {

    public static void main(String[] args) {

        com.formdev.flatlaf.themes.FlatMacLightLaf.setup();
        javax.swing.UIManager.put("Button.arc", 15);
        javax.swing.UIManager.put("Component.arc", 15);
        javax.swing.UIManager.put("TextComponent.arc", 15);

        SwingUtilities.invokeLater(MasterFrame::new);

    }

}