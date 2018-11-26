package dialogs;

import javax.swing.*;
import java.io.File;


public class PickIconDialog extends JDialog {
    private PickIconInterface mInterface;

    public void pickIcon() {
        JFileChooser jFileChooser = new JFileChooser();
        setBounds(50, 50, 520, 430);
        jFileChooser.showOpenDialog(this);
        File file = jFileChooser.getSelectedFile();


        if (mInterface != null && file != null)
            mInterface.pickIconFinished(file.getPath());
    }

    public void setInterface(PickIconInterface pickIconInterface) {
        this.mInterface = pickIconInterface;
    }

    public interface PickIconInterface {
        void pickIconFinished(String image);
    }

}

