package dialogs;

import javax.swing.*;
import java.io.File;

public class ChooseSaveDialog extends JDialog {
    private ChooseSaveInterface mInterface;

    public void chooseSave() {
        JFileChooser jFileChooser = new JFileChooser();
        setBounds(50, 50, 520, 430);
        int returnVal = jFileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            if (mInterface != null && file != null)
                mInterface.chooseSaveFinished(file.getParent(), file.getName());
        }
    }

    public void setInterface(ChooseSaveInterface chooseSaveInterface) {
        this.mInterface = chooseSaveInterface;
    }

    public interface ChooseSaveInterface {
        void chooseSaveFinished(String path, String name);
    }
}
