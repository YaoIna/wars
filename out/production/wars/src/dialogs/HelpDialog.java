package dialogs;


import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HelpDialog extends JDialog {

    public HelpDialog(String helpText) {
        setBounds(70, 70, 400, 250);
        setTitle("Help");
        JTextArea helpArea = Utils.getTextArea();
        helpArea.setFont(new Font("Dialog", Font.PLAIN, 15));
        helpArea.setText(helpText);
        helpArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(new JScrollPane(helpArea), BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

}
