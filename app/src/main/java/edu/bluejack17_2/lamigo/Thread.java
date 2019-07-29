package edu.bluejack17_2.lamigo;
import java.util.Random;

public class Thread {
    public String id;
    public String title;
    public String score;
    public String date;
    public String desc;
    public String userid;
    public String author;
    public String language;

    public Thread(){

    }

    public Thread(String id,String title, String score, String date , String desc){
        this.id = id;
        this.title = title;
        this.score = score;
        this.date = date;
        this.desc = desc;
    }

    public Thread(String id,String title, String score, String date , String desc, String userid , String author , String lang){
        this.id = id;
        this.title = title;
        this.score = score;
        this.date = date;
        this.desc = desc;
        this.userid = userid;
        this.author = author;
        this.language = lang;
    }

    public String getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String generateID() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String id = salt.toString();
        return id;

    }
}
