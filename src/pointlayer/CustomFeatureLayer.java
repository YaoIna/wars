package pointlayer;

import com.esri.mo2.cs.geom.BasePointsArray;
import com.esri.mo2.data.feat.*;
import com.esri.mo2.map.dpy.BaseFeatureLayer;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.draw.RasterMarkerSymbol;
import com.esri.mo2.map.draw.SimpleMarkerSymbol;
import com.esri.mo2.map.mem.MemoryFeatureClass;
import models.CSVModel;
import utils.Constants;
import utils.Utils;

import java.util.HashMap;
import java.util.Vector;

public class CustomFeatureLayer extends BaseFeatureLayer {
    private Vector<BaseFeature> mFeatureVector;
    private BaseFields mFields;

    public CustomFeatureLayer(CSVModel model, String layerName) {
        initFeatureVectorAndFields(model, layerName);
        setFeatureClass(getFeatureClass(layerName, model.getBasePointArray()));
        renderPointsSymbol();
    }

    private void renderPointsSymbol() {
        BaseSimpleRenderer srd = new BaseSimpleRenderer();
        RasterMarkerSymbol rasterMarkerSymbol = new RasterMarkerSymbol();
        rasterMarkerSymbol.setAntialiasing(true);
        rasterMarkerSymbol.setImageString(Utils.getImagePath("battle.png"));
        rasterMarkerSymbol.setSizeX(40);
        rasterMarkerSymbol.setSizeY(40);
        srd.setSymbol(rasterMarkerSymbol);
        setRenderer(srd);
        setCapabilities(new CustomFeatureLayer.XYLayerCapabilities());
    }

    private void initFeatureVectorAndFields(CSVModel model, String sourceName) {
        mFeatureVector = new Vector<>();
        mFields = new BaseFields();
        mFields.addField(new BaseField("#SHAPE#", Field.ESRI_SHAPE, 0, 0));
        mFields.addField(new BaseField("ID", java.sql.Types.INTEGER, 9, 0));
        mFields.addField(new BaseField(Constants.BATTLE, java.sql.Types.VARCHAR, 30, 0));
        mFields.addField(new BaseField(Constants.COUNTRY, java.sql.Types.VARCHAR, 30, 0));
        mFields.addField(new BaseField(Constants.TIME, java.sql.Types.VARCHAR, 30, 0));
        mFields.addField(new BaseField(Constants.CASUALTIES, java.sql.Types.VARCHAR, 30, 0));

        BasePointsArray basePointsArray = model.getBasePointArray();
        HashMap<String, Vector<String>> attrMap = model.getAttrMap();
        if (basePointsArray == null || attrMap == null)
            return;
        for (int i = 0; i < basePointsArray.size(); i++) {
            BaseFeature feature = new BaseFeature();
            feature.setFields(mFields);
            com.esri.mo2.cs.geom.Point p = new
                    com.esri.mo2.cs.geom.Point(basePointsArray.getPoint(i));
            feature.setValue(0, p);
            feature.setValue(1, 0);
            feature.setValue(2, attrMap.get(Constants.BATTLE).elementAt(i));
            feature.setValue(3, attrMap.get(Constants.COUNTRY).elementAt(i));
            feature.setValue(4, attrMap.get(Constants.TIME).elementAt(i));
            feature.setValue(5, attrMap.get(Constants.CASUALTIES).elementAt(i));
            feature.setDataID(new BaseDataID(sourceName, i));
            mFeatureVector.addElement(feature);
        }
    }

    private BaseFeatureClass getFeatureClass(String name, BasePointsArray basePointsArray) {
        MemoryFeatureClass feature = null;
        try {
            feature = new MemoryFeatureClass(MapDataset.POINT, mFields);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        if (feature != null) {
            feature.setName(name);
            for (int i = 0; i < basePointsArray.size(); i++) {
                feature.addFeature(mFeatureVector.elementAt(i));
            }
        }
        return feature;
    }


    private final class XYLayerCapabilities extends com.esri.mo2.map.dpy.LayerCapabilities {
        XYLayerCapabilities() {
            for (int i = 0; i < this.size(); i++) {
                setAvailable(getCapabilityName(i), true);
                setEnablingAllowed(getCapabilityName(i), true);
                getCapability(i).setEnabled(true);
            }
        }
    }
}
