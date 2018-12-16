package utils;

import com.esri.mo2.cs.geom.Point;
import com.esri.mo2.ui.bean.Layer;
import com.esri.mo2.ui.bean.Map;

import javax.swing.*;

public class Utils {

    public static void addShapeFileToMap(Map map, String path) {
        Layer layer = new Layer();
        String dataPath = "0;" + path;
        layer.setDataset(dataPath);
        map.add(layer);
    }

    public static double getDistanceFromWorldPoints(Point startPoint, Point endPoint) {
        return (69.44 / (2 * Math.PI)) * 360 * Math.acos(
                Math.sin(startPoint.y * 2 * Math.PI / 360)
                        * Math.sin(endPoint.y * 2 * Math.PI / 360)
                        + Math.cos(startPoint.y * 2 * Math.PI / 360)
                        * Math.cos(endPoint.y * 2 * Math.PI / 360)
                        * (Math.abs(startPoint.x - endPoint.x) < 180 ?
                        Math.cos((startPoint.x - endPoint.x) * 2 * Math.PI / 360) :
                        Math.cos((360 - Math.abs(startPoint.x - endPoint.x)) * 2 * Math.PI / 360)));
    }

    public static String getImagePath(String imageName) {
        return getDefaultRootPath() + "/icon/" + imageName;
    }

    public static String getDefaultRootPath() {
        return System.getProperty("user.dir");
    }

    public static String getBattlePic(String name) {
        return getDefaultRootPath() + "/data/pics/" + name;
    }

    public static JTextArea getTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

}
