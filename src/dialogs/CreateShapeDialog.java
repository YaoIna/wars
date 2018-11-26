package dialogs;

import com.esri.mo2.file.shp.ShapefileWriter;
import com.esri.mo2.map.dpy.FeatureLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateShapeDialog extends JDialog implements ActionListener {
    private String mName;
    private String mPath;
    private JButton mCancelButton;
    private JTextField mNameField;
    private FeatureLayer mSelectedLayer;
    private int mShpType = 2;

    public CreateShapeDialog(FeatureLayer layer, String path, int shpType) {
        JButton okButton = new JButton("OK");
        mCancelButton = new JButton("Cancel");
        mNameField = new JTextField("enter layer name here, then hit ENTER", 25);
        JPanel panel = new JPanel();
        JLabel centerLabel = new JLabel();

        mPath = path;
        mSelectedLayer = layer;
        mShpType = shpType;

        setBounds(40, 350, 450, 150);
        setTitle("Create new shapefile?");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });

        mNameField.addActionListener(this);
        okButton.addActionListener(this);
        mCancelButton.addActionListener(this);

        String s = "<HTML> To make a new shapefile from the new layer, enter<BR>" +
                "the new name you want for the layer and click OK.<BR>" +
                "You can then add it to the map in the usual way.<BR>" +
                "Click ENTER after replacing the text with your layer name";
        centerLabel.setHorizontalAlignment(JLabel.CENTER);
        centerLabel.setText(s);
        getContentPane().add(centerLabel, BorderLayout.CENTER);
        panel.add(mNameField);
        panel.add(okButton);
        panel.add(mCancelButton);
        getContentPane().add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == mNameField) {
            mName = mNameField.getText().trim();
            System.out.println(mPath + "    " + mPath);
        } else if (source == mCancelButton)
            setVisible(false);
        else {
            try {
                mName = mNameField.getText().trim();
                if (mName.length() == 0 || mPath == null)
                    return;
                ShapefileWriter.writeFeatureLayer(mSelectedLayer, mPath, mName, mShpType);
            } catch (Exception exception) {
                exception.printStackTrace();
                System.out.println("write error");
            }
            setVisible(false);
        }
    }
}
