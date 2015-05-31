package me.lingxiao.exam.model;


public class CyxbsInfo {

    private String versionCode;
    private String versionName;
    private String updateContent;
    private String apkURL;

    public CyxbsInfo(String versionCode, String versionName, String updateContent, String apkURL) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.updateContent = updateContent;
        this.apkURL = apkURL;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getApkURL() {
        return apkURL;
    }

    public void setApkURL(String apkURL) {
        this.apkURL = apkURL;
    }

}
