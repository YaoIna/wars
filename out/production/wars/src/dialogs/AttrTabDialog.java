package dialogs;

import com.esri.mo2.data.feat.*;
import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.map.dpy.Layer;
import models.AttrTabModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

public class AttrTabDialog extends JDialog {
    private FeatureLayer mFeatureLayer;
    private JTable mJTable;
    private AttrDismissInterface mInterface;
    private AttrTableSelectionListener mListener = new AttrTableSelectionListener();

    public AttrTabDialog(Layer layer) {
        mFeatureLayer = (FeatureLayer) layer;
        mJTable = new JTable(new AttrTabModel(mFeatureLayer));
        mListener = new AttrTableSelectionListener();
        JScrollPane jScroll = new JScrollPane(mJTable);
        setBounds(70, 70, 550, 400);
        setTitle("Attribute Table");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (mInterface != null)
                    mInterface.onAttrDismissed(mFeatureLayer.getName());
                setVisible(false);
            }
        });
        jScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        if (!mFeatureLayer.hasSelection() || mFeatureLayer.getSelectionSet() == null)
            initSelection();
        mJTable.getSelectionModel().addListSelectionListener(mListener);
        updateTableFromMapSelection();

        for (int i = 0; i < mJTable.getColumnCount(); i++) {
            mJTable.getColumnModel().getColumn(i).setMinWidth(80);
        }
        getContentPane().add(jScroll, BorderLayout.CENTER);
    }


    private void initSelection() {
        BaseQueryFilter baseQueryFilter = new BaseQueryFilter();
        FeatureClass featureClass = mFeatureLayer.getFeatureClass();
        Fields fields = featureClass.getFields();
        baseQueryFilter.setSubFields(fields);
        mFeatureLayer.setSelectedFeatures(baseQueryFilter);
        BaseSelectionSet selectionSet = (BaseSelectionSet) mFeatureLayer.getSelectionSet();
        selectionSet.clear();
        mFeatureLayer.setSelectionSet(selectionSet);
    }

    public void updateTableFromMapSelection() {
        if (mFeatureLayer.hasSelection()) {
            SelectionSet selectionSet = mFeatureLayer.getSelectionSet();
            mJTable.setVisible(false);
            ListSelectionModel listSelectionModel = mJTable.getSelectionModel();
            listSelectionModel.removeListSelectionListener(mListener);
            listSelectionModel.clearSelection();
            Iterator iterator = selectionSet.iterator();
            while (iterator.hasNext()) {
                BaseDataID baseDataID = (BaseDataID) iterator.next();
                listSelectionModel.addSelectionInterval(baseDataID.getID() - 1, baseDataID.getID() - 1);

            }
            mJTable.updateUI();
            mJTable.setVisible(true);
            listSelectionModel.addListSelectionListener(mListener);
        }
    }

    public void clear() {
        mJTable.getSelectionModel().clearSelection();
    }

    public void setInterface(AttrDismissInterface dissmissInterface) {
        this.mInterface = dissmissInterface;
    }

    class AttrTableSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!mFeatureLayer.hasSelection() || mFeatureLayer.getSelectionSet() == null)
                initSelection();
            int indexStart = mJTable.getSelectedRow();
            int indexEnd = mJTable.getSelectionModel().getMaxSelectionIndex();
            BaseSelectionSet baseSelectionSet = (BaseSelectionSet) mFeatureLayer.getSelectionSet();
            if (mFeatureLayer.hasSelection())
                baseSelectionSet.clear();
            for (int i = indexStart; i <= indexEnd; i++) {
                if (mJTable.isRowSelected(i)) {
                    BaseDataID caps = new BaseDataID("", i + 1);
                    baseSelectionSet.add(caps);
                }
            }
            mFeatureLayer.setSelectionSet(baseSelectionSet);
        }
    }

    public interface AttrDismissInterface {
        void onAttrDismissed(String name);
    }
}
