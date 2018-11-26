package models;

import com.esri.mo2.cs.geom.BasePointsArray;
import utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public class CSVModel {
    private BasePointsArray mBasePointArray;
    private HashMap<String, Vector<String>> mAttrMap;

    public void initCSVModel(File file) {
        if (file == null)
            return;
        mBasePointArray = new BasePointsArray();
        Vector<String> battleName = new Vector<>();
        Vector<String> country = new Vector<>();
        Vector<String> battleTime = new Vector<>();
        Vector<String> casualties = new Vector<>();
        mAttrMap = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            double x, y;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                x = Double.parseDouble(st.nextToken());
                y = Double.parseDouble(st.nextToken());
                mBasePointArray.insertPoint(count++, new com.esri.mo2.cs.geom.Point(x, y));
                battleName.addElement(st.nextToken());
                country.addElement(st.nextToken());
                battleTime.addElement(st.nextToken());
                casualties.addElement(st.nextToken());
            }
            mAttrMap.put(Constants.BATTLE, battleName);
            mAttrMap.put(Constants.COUNTRY, country);
            mAttrMap.put(Constants.TIME, battleTime);
            mAttrMap.put(Constants.CASUALTIES, casualties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BasePointsArray getBasePointArray() {
        return mBasePointArray;
    }

    public HashMap<String, Vector<String>> getAttrMap() {
        return mAttrMap;
    }
}
