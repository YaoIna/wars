package threads;

import com.esri.mo2.ui.bean.Legend;

public class Flash extends Thread {
    private Legend mLegend;
    private int mFlashTimes;

    public Flash(Legend legend, int times) {
        mLegend = legend;
        mFlashTimes = times;
    }

    public void run() {
        if (mLegend == null)
            return;
        for (int i = 0; i < mFlashTimes; i++) {
            try {
                Thread.sleep(500);
                mLegend.toggleSelected();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
