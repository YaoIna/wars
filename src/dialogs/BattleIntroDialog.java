package dialogs;

import models.BattleModel;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BattleIntroDialog extends JDialog implements ActionListener {

    private BattleModel mBattleModel;

    public BattleIntroDialog(JFrame frame, BattleModel battleModel) {
        super(frame, "Battle Introduction", true);
        setSize(700, 600);

        mBattleModel = battleModel;

        JLabel battleNameLabel = new JLabel();
        battleNameLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        battleNameLabel.setText(mBattleModel.getBattleName());
        battleNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel battlePicLabel = new JLabel(new ImageIcon(mBattleModel.getBattlePicPath()));
        battlePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea battleIntroTextArea = getTextArea();
        battleIntroTextArea.setFont(new Font("Dialog", Font.PLAIN, 15));
        battleIntroTextArea.setText(mBattleModel.getBattleIntro());
        battleIntroTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton linkButton = new JButton();
        linkButton.setText("<HTML>Click the <FONT color=\"#000099\"><U>link</U></FONT>"
                + " to go to the Java website.</HTML>");
        linkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        linkButton.addActionListener(this);

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(battleNameLabel);
        verticalBox.add(Box.createVerticalStrut(20));
        verticalBox.add(battlePicLabel);
        verticalBox.add(Box.createVerticalStrut(20));
        verticalBox.add(battleIntroTextArea);
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(linkButton);

        getContentPane().add(verticalBox, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });


    }


    private JTextArea getTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            URI uri = new URI(mBattleModel.getBattleLink());
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
}



