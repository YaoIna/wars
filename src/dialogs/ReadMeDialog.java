package dialogs;

import utils.Constants;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ReadMeDialog extends JDialog {

    public ReadMeDialog() {
        setSize(700, 600);
        setTitle("Read Me");

        JTextArea readMeArea = Utils.getTextArea();
        readMeArea.setFont(new Font("Dialog", Font.PLAIN, 15));
        readMeArea.setText(Constants.READ_ME);
        readMeArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel battlePicLabel = new JLabel(new ImageIcon(Utils.getBattlePic("pet_with_me.jpg")));
        battlePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(readMeArea);
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(battlePicLabel);

        getContentPane().add(verticalBox, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}
