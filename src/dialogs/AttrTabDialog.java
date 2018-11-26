package dialogs;

import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.map.dpy.Layer;
import models.AttrTabModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AttrTabDialog extends JDialog {

    public AttrTabDialog(Layer layer) {
        JTable jTable = new JTable(new AttrTabModel((FeatureLayer) layer));
        JScrollPane jScroll = new JScrollPane(jTable);
        setBounds(70, 70, 550, 400);
        setTitle("Attribute Table");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        jScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < jTable.getColumnCount(); i++) {
            jTable.getColumnModel().getColumn(i).setMinWidth(80);
        }
        getContentPane().add(jScroll, BorderLayout.CENTER);
    }
}
