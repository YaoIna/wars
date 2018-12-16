package models;

import com.esri.mo2.data.feat.*;
import com.esri.mo2.map.dpy.FeatureLayer;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class AttrTabModel extends AbstractTableModel {

    private Fields mFields;
    private ArrayList<ArrayList<String>> mData;

    public AttrTabModel(FeatureLayer layer) {
        if (layer == null)
            return;
        mData = new ArrayList<>();
        FeatureClass featureClass = layer.getFeatureClass();
        mFields = featureClass.getFields();
        BaseQueryFilter baseQueryFilter = new BaseQueryFilter();
        baseQueryFilter.setSubFields(mFields);
        Cursor cursor = layer.search(baseQueryFilter);
        int row = 0;
        while (cursor.hasMore()) {
            ArrayList<String> inner = new ArrayList<>();
            Feature feature = (Feature) cursor.next();
            inner.add(0, String.valueOf(row));
            for (int i = 1; i < mFields.size(); i++) {
                inner.add(feature.getValue(i).toString());
            }
            mData.add(inner);
            row++;
        }
    }

    @Override
    public int getRowCount() {
        return mData.size();
    }

    @Override
    public int getColumnCount() {
        return mFields.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return mData.get(rowIndex).get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return mFields.getNames()[column];
    }

}
