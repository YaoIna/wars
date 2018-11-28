package main;

import com.esri.mo2.cs.geom.Envelope;
import com.esri.mo2.data.feat.BaseFeature;
import com.esri.mo2.data.feat.Cursor;
import com.esri.mo2.data.feat.Data;
import com.esri.mo2.file.shp.ShapefileFolder;
import com.esri.mo2.file.shp.ShapefileWriter;
import com.esri.mo2.map.dpy.*;
import com.esri.mo2.map.dpy.Layer;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.draw.RasterMarkerSymbol;
import com.esri.mo2.map.draw.SimpleFillSymbol;
import com.esri.mo2.ui.bean.*;
import com.esri.mo2.ui.bean.LayerNotFoundException;
import com.esri.mo2.ui.ren.LayerProperties;
import com.esri.mo2.ui.ren.Util;
import com.esri.mo2.ui.tb.SelectionToolBar;
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import data.DataRepo;
import dialogs.*;
import models.BattleModel;
import models.CSVModel;
import pointlayer.CustomFeatureLayer;
import threads.Flash;
import utils.DistanceTool;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class MainWarsMap extends JFrame implements AddLayerDialog.AddLayerInterface,
        DistanceTool.DragPointsInterface, PickFileDialog.PickFileInterface, ChooseSaveDialog.ChooseSaveInterface {

    private final String PICK_ICON = "PICK_ICON";
    private final String HOTLINK_CURSOR = "HOTLINK_CURSOR";

    private Map mMap;
    private SelectionToolBar mSelectionToolBar;
    private ZoomPanToolBar mZoomPanToolBar;
    private Toc mToc;
    private Legend mCurrentLegend;
    private Layer mActiveLayer;
    private JPanel mHeadJPanel = new JPanel();
    private TocAdapter mTocAdapter;


    private JMenuItem mPromoteMenuItem;
    private JMenuItem mDemoteMenuItem;
    private JMenuItem mPickIconMenuItem;
    private JMenuItem mRemoveLayerMenuItem;
    private JMenuItem mLegendMenuItem;
    private JMenuItem mAttrMenuItem;
    private JMenuItem mCreateLayerMenuItem;
    private JMenuItem mCreateShapefileFromCSV;

    private JButton mPickIconButton;
    private JButton mHotlinkButton;

    private Identify mHotlinkIdentify;

    private JLabel mMilesLabel;
    private JLabel mKMLabel;
    private AcetateLayer mDistanceAcetateLayer;

    private int mActiveLayoutIndex;
    private boolean mFullMap = true;
    private Envelope mEnvelope;


    public MainWarsMap() {
        super("MainWarsMap");
        this.setBounds(20, 20, 1400, 900);
        mMap = new Map();
        mSelectionToolBar = new SelectionToolBar();
        mZoomPanToolBar = new ZoomPanToolBar();
        mSelectionToolBar.setMap(mMap);
        mZoomPanToolBar.setMap(mMap);
        resizeMap();

        //it will be invalid if i put codes in a method
        mHotlinkIdentify = new Identify();
        setHotlinkCursor(mHotlinkIdentify);
        PickListener listener = new PickListener() {
            @Override
            public void beginPick(PickEvent pickEvent) {
            }

            @Override
            public void foundData(PickEvent pickEvent) {
                Cursor cursor = pickEvent.getCursor();
                Data data = cursor.next();
                if (data instanceof BaseFeature) {
                    BaseFeature baseFeature = (BaseFeature) data;
                    showBattleIntro(baseFeature.getDataID().getID());
                }
            }

            @Override
            public void endPick(PickEvent pickEvent) {
            }
        };
        mHotlinkIdentify.addPickListener(listener);
        mHotlinkIdentify.setPickWidth(20);

        initMenuBar();
        initJButtons();
        initLabelsStatus();
        mHeadJPanel.add(mSelectionToolBar);
        mHeadJPanel.add(mZoomPanToolBar);
        getContentPane().add(mHeadJPanel, BorderLayout.NORTH);

        getContentPane().add(mMap, BorderLayout.CENTER);
        Utils.addShapeFileToMap(mMap, "/Users/xxy/Desktop/IdeaProjects/wars/data/ShapeFile/country.shp");
        initToc();

        addBattlesLayer();

    }


    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu themeMenu = new JMenu("Theme");
        JMenu layerControlMenu = new JMenu("LayerControl");

        mPromoteMenuItem = new JMenuItem("promote selected layer",
                new ImageIcon(Utils.getImagePath("promote.jpg")));
        mDemoteMenuItem = new JMenuItem("demote selected layer",
                new ImageIcon(Utils.getImagePath("demote.jpg")));
        mPickIconMenuItem = new JMenuItem("pick icons", new ImageIcon(Utils.getImagePath("pick.png")));
        mPromoteMenuItem.setActionCommand(PICK_ICON);
        mPickIconMenuItem.setToolTipText("Pick a icon for point layer");
        mRemoveLayerMenuItem = new JMenuItem("remove layer", new ImageIcon(Utils.getImagePath("delete.gif")));
        mLegendMenuItem = new JMenuItem("Legend Editor", new ImageIcon(Utils.getImagePath("properties.gif")));
        mAttrMenuItem = new JMenuItem("open attribute table", new ImageIcon(Utils.getImagePath("tableview.gif")));
        mCreateLayerMenuItem = new JMenuItem("create layer from selection",
                new ImageIcon(Utils.getImagePath("Icon0915b.jpg")));
        mCreateShapefileFromCSV = new JMenuItem("create shapefile from CSV",
                new ImageIcon(Utils.getImagePath("Icon0915b.jpg")));
        JMenuItem printMenuItem = new JMenuItem("print", new ImageIcon(Utils.getImagePath("print.gif")));
        JMenuItem addLayerMenuItem = new JMenuItem("add layer", new ImageIcon("addtheme.gif"));
        JMenuItem[] menuItemArray = {addLayerMenuItem, mRemoveLayerMenuItem, mLegendMenuItem, mAttrMenuItem,
                mCreateLayerMenuItem, mCreateShapefileFromCSV, mPickIconMenuItem, mPromoteMenuItem, mDemoteMenuItem, printMenuItem};

        final String[] actionArray = {"add_layer", "remove_layer", "legend_editor", "attr_menu", "create_layer_file", "create_shapefile_csv", PICK_ICON, "promote", "demote", "print"};
        ActionListener menuItemListener = e -> {
            switch (e.getActionCommand()) {
                case "add_layer":
                    addLayer();
                    break;
                case "remove_layer":
                    Layer dpyLayer = mCurrentLegend.getLayer();
                    mMap.getLayerset().removeLayer(dpyLayer);
                    mMap.redraw();
                    resetMenuItems();
                    mSelectionToolBar.setSelectedLayer(null);
                    mZoomPanToolBar.setSelectedLayer(null);
                    if (mPickIconButton != null)
                        mPickIconButton.setEnabled(false);
                    if (mHotlinkButton != null)
                        mHotlinkButton.setEnabled(false);
                    break;
                case "legend_editor":
                    LayerProperties lp = new LayerProperties();
                    lp.setLegend(mCurrentLegend);
                    lp.setSelectedTabIndex(0);
                    lp.setVisible(true);
                    break;
                case "attr_menu":
                    AttrTabDialog attrTab = new AttrTabDialog(mCurrentLegend.getLayer());
                    attrTab.setVisible(true);
                    break;
                case "create_layer_file":
                    createLayerFile();
                    break;
                case "create_shapefile_csv":
                    chooseShapefilePath();
                    break;
                case PICK_ICON:
                    pickImageFile();
                    break;
                case "promote":
                case "demote":
                    if (e.getActionCommand().equals("promote"))
                        mMap.getLayerset().moveLayer(mActiveLayoutIndex, ++mActiveLayoutIndex);
                    else {
                        mMap.getLayerset().moveLayer(mActiveLayoutIndex, --mActiveLayoutIndex);
                    }
                    enableLayerControlItems();
                    mMap.redraw();
                    break;
                case "print":
                    print();
                    break;
                default:
                    break;
            }
        };
        for (int i = 0; i < actionArray.length; i++) {
            menuItemArray[i].setActionCommand(actionArray[i]);
            menuItemArray[i].addActionListener(menuItemListener);
        }
        resetMenuItems();

        setJMenuBar(menuBar);
        fileMenu.add(addLayerMenuItem);
        fileMenu.add(printMenuItem);
        fileMenu.add(mRemoveLayerMenuItem);
        fileMenu.add(mLegendMenuItem);
        themeMenu.add(mAttrMenuItem);
        themeMenu.add(mCreateLayerMenuItem);
        themeMenu.add(mCreateShapefileFromCSV);
        themeMenu.add(mPickIconMenuItem);
        layerControlMenu.add(mPromoteMenuItem);
        layerControlMenu.add(mDemoteMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(themeMenu);
        menuBar.add(layerControlMenu);
    }

    private void createLayerFile() {
        BaseSimpleRenderer baseSimpleRenderer = new BaseSimpleRenderer();
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol();
        simpleFillSymbol.setSymbolColor(new Color(255, 255, 0));
        simpleFillSymbol.setType(com.esri.mo2.map.draw.SimpleFillSymbol.FILLTYPE_SOLID);
        simpleFillSymbol.setBoundary(true);
        FeatureLayer featureLayer = (FeatureLayer) mCurrentLegend.getLayer();
        System.out.println("has selected" + featureLayer.hasSelection());
        if (featureLayer.hasSelection()) {
            FeatureLayer selectedLayer = featureLayer.createSelectionLayer(featureLayer.getSelectionSet());
            baseSimpleRenderer.setLayer(selectedLayer);
            baseSimpleRenderer.setSymbol(simpleFillSymbol);
            selectedLayer.setRenderer(baseSimpleRenderer);
            Layerset layerset = mMap.getLayerset();
            layerset.addLayer(selectedLayer);
            if (mSelectionToolBar.getSelectedLayers() != null)
                mPromoteMenuItem.setEnabled(true);
            Layer layer = mCurrentLegend.getLayer();
            LayerSource source = layer.getLayerSource();
            String path;
            if (source == null)
                path = Utils.getDefaultRootPath() + "/data/ShapeFile";
            else {
                path = ((ShapefileFolder) (mCurrentLegend.getLayer().getLayerSource())).getPath();
            }
            int shpType = 2;
            if (selectedLayer instanceof CustomFeatureLayer)
                shpType = 0;
            CreateShapeDialog csd = new CreateShapeDialog(selectedLayer, path, shpType);
            csd.setVisible(true);
            try {
                Flash flash = new Flash(mToc.findLegend(selectedLayer), 12);
                flash.start();
            } catch (LayerNotFoundException e) {
                e.printStackTrace();
            }
            mMap.redraw();
        }
    }


    private void initToc() {
        if (mToc == null)
            mToc = new Toc();
        if (mTocAdapter == null)
            mTocAdapter = new TocAdapter() {
                @Override
                public void click(TocEvent tocEvent) {
                    System.out.println(mActiveLayoutIndex + " dex");
                    mCurrentLegend = tocEvent.getLegend();
                    mActiveLayer = mCurrentLegend.getLayer();
                    mSelectionToolBar.setSelectedLayer(mActiveLayer);
                    mZoomPanToolBar.setSelectedLayer(mActiveLayer);
                    mActiveLayoutIndex = mMap.getLayerset().indexOf(mActiveLayer);
                    System.out.println(mActiveLayoutIndex + " active index");
                    mRemoveLayerMenuItem.setEnabled(true);
                    mLegendMenuItem.setEnabled(true);
                    mAttrMenuItem.setEnabled(true);
                    mCreateLayerMenuItem.setEnabled(true);
                    enableLayerControlItems();


                    if (mActiveLayer instanceof BaseFeatureLayer || mActiveLayer instanceof FeatureLayer) {
                        mPickIconMenuItem.setEnabled(true);
                        mPickIconButton.setEnabled(true);
                    } else {
                        mPickIconMenuItem.setEnabled(false);
                        mPickIconButton.setEnabled(false);
                    }

                    if (mActiveLayer instanceof CustomFeatureLayer) {
                        mCreateShapefileFromCSV.setEnabled(true);
                        mHotlinkButton.setEnabled(true);
                        Layer[] layers = {mActiveLayer};
                        mHotlinkIdentify.setSelectedLayers(layers);

                    } else {
                        mCreateShapefileFromCSV.setEnabled(false);
                        mHotlinkButton.setEnabled(false);
                    }
                }
            };
        mToc.setMap(mMap);
        mToc.addTocListener(mTocAdapter);
        getContentPane().add(mToc, BorderLayout.WEST);
    }

    private void enableLayerControlItems() {
        int layerCount = mMap.getLayerset().size();
        if (layerCount < 2) {
            mPromoteMenuItem.setEnabled(false);
            mDemoteMenuItem.setEnabled(false);
        } else if (mActiveLayoutIndex == 0) {
            mDemoteMenuItem.setEnabled(false);
            mPromoteMenuItem.setEnabled(true);
        } else if (mActiveLayoutIndex == layerCount - 1) {
            mPromoteMenuItem.setEnabled(false);
            mDemoteMenuItem.setEnabled(true);
        } else {
            mPromoteMenuItem.setEnabled(true);
            mDemoteMenuItem.setEnabled(true);
        }
    }

    private void addBattlesLayer() {
        File battleFile = new File("/Users/xxy/Desktop/IdeaProjects/wars/data/csv_battles.csv");
        CSVModel model = new CSVModel();
        model.initCSVModel(battleFile);
        CustomFeatureLayer warLayer = new CustomFeatureLayer(model, "Battles");
        warLayer.setVisible(true);
        mMap.getLayerset().addLayer(warLayer);
        mMap.redraw();
    }

    private void print() {
        Print print = new Print();
        print.setMap(mMap);
        print.doPrint();
    }

    private void addLayer() {
        AddLayerDialog dialog = new AddLayerDialog();
        dialog.setInterface(MainWarsMap.this);
        dialog.setVisible(true);
    }

    private void chooseShapefilePath() {
        ChooseSaveDialog chooseSaveDialog = new ChooseSaveDialog();
        chooseSaveDialog.setInterface(this);
        chooseSaveDialog.chooseSave();
    }

    private void pickImageFile() {
        PickFileDialog pickFileDialog = new PickFileDialog();
        pickFileDialog.setInterface(this);
        pickFileDialog.pickFile();
    }

    private void pickIcon(String imagePath) {
        if (mActiveLayer instanceof BaseFeatureLayer) {
            BaseFeatureLayer layer = (BaseFeatureLayer) mActiveLayer;
            BaseSimpleRenderer srd = new BaseSimpleRenderer();
            RasterMarkerSymbol rasterMarkerSymbol = new RasterMarkerSymbol();
            rasterMarkerSymbol.setAntialiasing(true);
            rasterMarkerSymbol.setImageString(imagePath);
            rasterMarkerSymbol.setSizeX(40);
            rasterMarkerSymbol.setSizeY(40);
            srd.setSymbol(rasterMarkerSymbol);
            layer.setRenderer(srd);
            mMap.getLayerset().removeLayer(layer);
            layer.setVisible(true);
            mMap.getLayerset().addLayer(layer);
            mMap.redraw();
        } else {
            mPickIconButton.setEnabled(false);
            mPickIconMenuItem.setEnabled(false);
        }
    }

    private void setHotlinkCursor(Identify hotlink) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(Utils.getImagePath("hotlink_cursor.png"));
        java.awt.Cursor cursor = toolkit.createCustomCursor(image, new Point(5, 5), HOTLINK_CURSOR);
        hotlink.setCursor(cursor);
    }


    private void resetMenuItems() {
        mDemoteMenuItem.setEnabled(false);
        mPromoteMenuItem.setEnabled(false);
        mRemoveLayerMenuItem.setEnabled(false);
        mLegendMenuItem.setEnabled(false);
        mAttrMenuItem.setEnabled(false);
        mPickIconMenuItem.setEnabled(false);
        mCreateLayerMenuItem.setEnabled(false);
        mCreateShapefileFromCSV.setEnabled(false);
    }

    private void initJButtons() {
        JToolBar jToolBar = new JToolBar();
        final String print = "print";
        final String pointer = "pointer";
        final String distance = "distance";
        final String addLayer = "add_layer";
        final String hotlink = "hotlink";

        JButton printButton = new JButton(new ImageIcon(Utils.getImagePath("print.gif")));
        printButton.setActionCommand(print);
        printButton.setToolTipText("print map");
        JButton pointerButton = new JButton(new ImageIcon(Utils.getImagePath("pointer.gif")));
        pointerButton.setActionCommand(pointer);
        pointerButton.setToolTipText("pointer");
        JButton distanceButton = new JButton(new ImageIcon(Utils.getImagePath("measure_1.gif")));
        distanceButton.setActionCommand(distance);
        distanceButton.setToolTipText("press-drag-release to measure a distance");
        mPickIconButton = new JButton(new ImageIcon(Utils.getImagePath("pick.png")));
        mPickIconButton.setActionCommand(PICK_ICON);
        mPickIconButton.setToolTipText("Pick a icon for point layer");
        mPickIconButton.setEnabled(false);
        mHotlinkButton = new JButton(new ImageIcon(Utils.getImagePath("hotlink.png")));
        mHotlinkButton.setActionCommand(hotlink);
        mHotlinkButton.setToolTipText("Click to use hotlink");
        mHotlinkButton.setEnabled(false);
        JButton addLayerButton = new JButton(new ImageIcon(Utils.getImagePath("addtheme.gif")));
        addLayerButton.setActionCommand(addLayer);
        addLayerButton.setToolTipText("add layer");
        Arrow arrow = new Arrow();
        DistanceTool distanceTool = new DistanceTool();


        ActionListener buttonListener = e -> {
            switch (e.getActionCommand()) {
                case print:
                    print();
                    break;
                case pointer:
                    mMap.setSelectedTool(arrow);
                    resetDistance();
                    break;
                case distance:
                    distanceTool.setInterface(MainWarsMap.this);
                    mMap.setSelectedTool(distanceTool);
                    break;
                case PICK_ICON:
                    pickImageFile();
                    break;
                case addLayer:
                    addLayer();
                    break;
                case hotlink:
                    mMap.setSelectedTool(mHotlinkIdentify);
                    break;
                default:
                    break;

            }
        };
        printButton.addActionListener(buttonListener);
        pointerButton.addActionListener(buttonListener);
        distanceButton.addActionListener(buttonListener);
        mPickIconButton.addActionListener(buttonListener);
        mHotlinkButton.addActionListener(buttonListener);
        addLayerButton.addActionListener(buttonListener);

        jToolBar.add(printButton);
        jToolBar.add(pointerButton);
        jToolBar.add(distanceButton);
        jToolBar.add(mPickIconButton);
        jToolBar.add(mHotlinkButton);
        jToolBar.add(addLayerButton);
        mHeadJPanel.add(jToolBar);
    }


    private void initLabelsStatus() {
        JPanel footJPanel = new JPanel();
        JLabel statusLabel = new JLabel("status bar    LOC");
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        mMilesLabel = new JLabel("   DIST:  0 mi    ");
        mKMLabel = new JLabel("  0 km    ");

        mMap.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me) {
                com.esri.mo2.cs.geom.Point worldPoint;
                if (mMap.getLayerCount() > 0) {
                    worldPoint = mMap.transformPixelToWorld(me.getX(), me.getY());
                    String s = "X:" + decimalFormat.format(worldPoint.getX()) + " " +
                            "Y:" + decimalFormat.format(worldPoint.getY());
                    statusLabel.setText(s);
                } else
                    statusLabel.setText("X:0.000 Y:0.000");
            }
        });

        footJPanel.add(statusLabel);
        footJPanel.add(mMilesLabel);
        footJPanel.add(mKMLabel);
        getContentPane().add(footJPanel, BorderLayout.SOUTH);
    }

    private void resizeMap() {
        ActionListener zoomListener = ae -> mFullMap = false; // can change a boolean here
        ((JButton) mZoomPanToolBar.getActionComponent("ZoomIn")).addActionListener(zoomListener);
        ((JButton) mZoomPanToolBar.getActionComponent("ZoomToFullExtent")).addActionListener(e -> mFullMap = true);
        ((JButton) mZoomPanToolBar.getActionComponent("ZoomToSelectedLayer")).addActionListener(zoomListener);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (mFullMap) {
                    mMap.setExtent(mEnvelope);
                    mMap.zoom(1.0);
                    mMap.redraw();
                }
            }
        });
    }

    private void showBattleIntro(int index) {
        if (index >= 0 && index < 20) {
            BattleModel battleModel = new BattleModel();
            battleModel.setBattleName(DataRepo.BATTLE_NAMES[index]);
            battleModel.setBattlePicPath(Utils.getBattlePic(index + ".jpg"));
            battleModel.setBattleIntro(DataRepo.BATTLE_INTRO[index]);
            battleModel.setBattleLink(DataRepo.BATTLES_LINKS[index]);

            BattleIntroDialog battleIntroDialog = new BattleIntroDialog(this, battleModel);
            battleIntroDialog.setVisible(true);
        }

    }


    private void resetDistance() {
        mMilesLabel.setText("DIST   0 mi   ");
        mKMLabel.setText("   0 km    ");
        if (mDistanceAcetateLayer != null)
            mMap.remove(mDistanceAcetateLayer);
        mDistanceAcetateLayer = null;
        mMap.repaint();
    }

    public void setMapEnv() {
        mEnvelope = mMap.getExtent();
    }

    @Override
    public void addLayerFinished(Layer layer) {
        mMap.getLayerset().addLayer(layer);
        mMap.redraw();
        if (mSelectionToolBar.getSelectedLayers() != null)
            mPromoteMenuItem.setEnabled(true);

    }

    @Override
    public void addLayerCanceled() {

    }

    @Override
    public void onDragPoints(Point startPoint, Point endPoint) {
        com.esri.mo2.cs.geom.Point startWorldPoint = mMap.transformPixelToWorld((int) startPoint.getX(), (int) startPoint.getY());
        com.esri.mo2.cs.geom.Point endWorldPoint = mMap.transformPixelToWorld((int) endPoint.getX(), (int) endPoint.getY());
        double distance = Utils.getDistanceFromWorldPoints(startWorldPoint, endWorldPoint);
        System.out.println(distance);
        mMilesLabel.setText("DIST: " + (float) distance + " mi  ");
        mKMLabel.setText((float) (distance * 1.6093) + " km");
        if (mDistanceAcetateLayer != null)
            mMap.remove(mDistanceAcetateLayer);
        mDistanceAcetateLayer = new AcetateLayer() {
            public void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                Line2D.Double line = new Line2D.Double(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                g2d.setColor(new Color(0, 0, 250));
                g2d.draw(line);
            }
        };
        mMap.add(mDistanceAcetateLayer);
        mMap.redraw();
    }


    @Override
    public void pickFileFinished(String filePath) {
        pickIcon(filePath);
    }

    @Override
    public void chooseSaveFinished(String path, String name) {
        if (mActiveLayer instanceof CustomFeatureLayer) {
            CustomFeatureLayer layer = (CustomFeatureLayer) mActiveLayer;
            try {
                ShapefileWriter.writeFeatureLayer(layer, path, name, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class Arrow extends Tool {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            super.mouseClicked(mouseEvent);
        }
    }


}


