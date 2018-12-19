package dialogs;

import com.esri.mo2.cs.geom.BasePointsArray;
import models.CSVModel;
import pointlayer.CustomFeatureLayer;

import javax.swing.*;
import java.io.File;

import java.util.Vector;


public class AddXYThemeDialog extends JDialog {
    private AddXYThemeInterface mInterface;

    public void addLayer(String layerName) {
        Vector<String> vector = new Vector<>();
        JFileChooser jFileChooser = new JFileChooser();
        BasePointsArray bpa = new BasePointsArray();

        setBounds(50, 50, 520, 430);
        jFileChooser.showOpenDialog(this);
        File file = jFileChooser.getSelectedFile();
        CSVModel model = new CSVModel();
        model.initCSVModel(file);


        if (mInterface != null)
            mInterface.addXYThemeFinished(new CustomFeatureLayer(model, layerName));
    }

    public void setInterface(AddXYThemeInterface addXYThemeInterface) {
        this.mInterface = addXYThemeInterface;
    }

    public interface AddXYThemeInterface {
        void addXYThemeFinished(CustomFeatureLayer layer);
    }

}
