package dialogs;

import com.esri.mo2.map.dpy.Layer;
import com.esri.mo2.ui.bean.CustomDatasetEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


public class AddLayerDialog extends JDialog implements ActionListener {
    private JButton mOkButton;
    private JButton mCancelButton;
    private CustomDatasetEditor mCustomDatasetEditor;
    private AddLayerInterface mInterface;

    public AddLayerDialog() {
        mOkButton = new JButton("OK");
        mCancelButton = new JButton("Cancel");

        JPanel panel = new JPanel();
        mCustomDatasetEditor = new com.esri.mo2.ui.bean.
                CustomDatasetEditor();

        setBounds(50, 50, 520, 430);
        setTitle("Select a theme/layer");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        mOkButton.addActionListener(this);
        mCancelButton.addActionListener(this);
        getContentPane().add(mCustomDatasetEditor, BorderLayout.CENTER);
        panel.add(mOkButton);
        panel.add(mCancelButton);
        getContentPane().add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == mCancelButton) {
            if (mInterface != null)
                mInterface.addLayerCanceled();
            setVisible(false);
        } else if (source == mOkButton) {
            try {
                if (mInterface != null)
                    mInterface.addLayerFinished(mCustomDatasetEditor.getLayer());
                setVisible(false);

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void setInterface(AddLayerInterface addLayerInterface) {
        this.mInterface = addLayerInterface;
    }

    public interface AddLayerInterface {
        void addLayerFinished(Layer layer);

        void addLayerCanceled();
    }
}
