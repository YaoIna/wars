package utils;

import com.esri.mo2.ui.bean.DragTool;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DistanceTool extends DragTool {
    private Point mStartPoint;
    private DragPointsInterface mInterface;

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        mStartPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        super.mouseReleased(mouseEvent);
        Point endPoint = new Point(mouseEvent.getX(), mouseEvent.getY());

        if (mInterface != null)
            mInterface.onDragPoints(mStartPoint, endPoint);
    }

    @Override
    public void cancel() {

    }

    public void setInterface(DragPointsInterface dragPointsInterface) {
        this.mInterface = dragPointsInterface;
    }


    public interface DragPointsInterface {
        void onDragPoints(Point startPoint, Point endPoint);
    }
}
