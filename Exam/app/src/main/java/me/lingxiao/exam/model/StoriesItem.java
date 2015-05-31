package me.lingxiao.exam.model;


import java.io.Serializable;

public class StoriesItem implements Serializable {

    private String title;
    private String date;
    private String content;
    private int votePositive;
    private int voteNegative;
    private int reply;
    private int id;

    public StoriesItem(String title, String date, String content, int votePositive, int voteNegative,
                       int reply, int id, String email) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.votePositive = votePositive;
        this.voteNegative = voteNegative;
        this.reply = reply;
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
