package models;

public class BattleModel {
    private String mBattleName;
    private String mBattlePicPath;
    private String mBattleIntro;
    private String mBattleLink;

    public BattleModel() {
        mBattleName = "";
        mBattlePicPath = "";
        mBattleIntro = "";
        mBattleLink = "";
    }


    public String getBattleName() {
        return mBattleName;
    }

    public String getBattlePicPath() {
        return mBattlePicPath;
    }

    public String getBattleIntro() {
        return mBattleIntro;
    }

    public String getBattleLink() {
        return mBattleLink;
    }

    public void setBattleName(String battleName) {
        this.mBattleName = battleName;
    }

    public void setBattlePicPath(String battlePicPath) {
        this.mBattlePicPath = battlePicPath;
    }

    public void setBattleIntro(String battleIntro) {
        this.mBattleIntro = battleIntro;
    }

    public void setBattleLink(String battleLink) {
        this.mBattleLink = battleLink;
    }
}
