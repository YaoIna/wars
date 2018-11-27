package dialogs;

import javax.swing.*;
import java.io.File;


public class PickFileDialog extends JDialog {
    private PickFileInterface mInterface;

    public void pickFile() {
        JFileChooser jFileChooser = new JFileChooser();
        setBounds(50, 50, 520, 430);
        int returnVal = jFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            if (mInterface != null && file != null)
                mInterface.pickFileFinished(file.getPath());
        }
    }

    public void setInterface(PickFileInterface pickFileInterface) {
        this.mInterface = pickFileInterface;
    }

    public interface PickFileInterface {
        void pickFileFinished(String filePath);
    }

}

