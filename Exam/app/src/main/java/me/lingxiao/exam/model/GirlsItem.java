package me.lingxiao.exam.model;

import android.graphics.Bitmap;

public class GirlsItem {
    private String title;
    private String date;
    private String picUrl;
    private int votePositive;
    private int voteNegative;
    private int reply;
    private Bitmap pic;
    private int id;

    public GirlsItem(String title, String date, String picUrl, int votePositive, int voteNegative,
                     int reply, int id) {
        this.title = title;
        this.date = date;
        this.picUrl = picUrl;
        this.votePositive = votePositive;
        this.voteNegative = voteNegative;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getVotePositive() {
        return votePositive;
    }

    public void setVotePositive(int votePositive) {
        this.votePositive = votePositive;
    }

    public int getVoteNegative() {
        return voteNegative;
    }

    public void setVoteNegative(int voteNegative) {
        this.voteNegative = voteNegative;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
