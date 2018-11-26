
import main.MainWarsMap;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        MainWarsMap addIconToPointLayer = new MainWarsMap();
        addIconToPointLayer.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("Thanks and bye~");
                System.exit(0);
            }
        });
        addIconToPointLayer.setVisible(true);
        addIconToPointLayer.setMapEnv();
    }
}
