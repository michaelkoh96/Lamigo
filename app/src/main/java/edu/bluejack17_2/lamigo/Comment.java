package edu.bluejack17_2.lamigo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Comment {
    public String id;
    public String threadId;
    public String points;
    public String date;
    public String desc;
    public String username;
    public String userid;

    public Comment(){

    }

    public Comment(String id, String threadId ,String points, String date , String desc, String userid, String username){
        this.id = id;
        this.threadId = threadId;
        this.points = points;
        this.date = date;
        this.desc = desc;
        this.userid = userid;
        this.username = username;
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

    public static ArrayList<Comment> sortByPoints(ArrayList<Comment> comments){
        ArrayList<Comment> sorted;

        //sort here
        if (comments.isEmpty()){
            return comments;
        }

        ArrayList<Comment> smaller = new ArrayList<Comment>();
        ArrayList<Comment> greater = new ArrayList<Comment>();
        Comment pivot = comments.get(0);
        int i;
        Comment j;
        for (i=1;i<comments.size();i++)
        {
            j=comments.get(i);
            if (Integer.parseInt(j.points) > Integer.parseInt(pivot.points))
                smaller.add(j);
            else
                greater.add(j);
        }
        smaller=sortByPoints(smaller);
        greater=sortByPoints(greater);
        smaller.add(pivot);
        smaller.addAll(greater);
        sorted = smaller;

        return sorted;
    }

    public static ArrayList<Comment> sortByDateNew(ArrayList<Comment> comments){
        ArrayList<Comment> sorted;

        //sort here
        if (comments.isEmpty()){
            return comments;
        }

        ArrayList<Comment> smaller = new ArrayList<Comment>();
        ArrayList<Comment> greater = new ArrayList<Comment>();
        Comment pivot = comments.get(0);
        int i;
        Comment j;
        Date d1 = new Date();
        Date d2 = new Date();
        long ld1,ld2;
        for (i=1;i<comments.size();i++)
        {
            j=comments.get(i);
            try{
                d1 = new SimpleDateFormat("dd/MM/yyyy").parse(j.date);
                d2 = new SimpleDateFormat("dd/MM/yyyy").parse(pivot.date);
            }catch (Exception e){

            }
            ld1 = d1.getTime();
            ld2 = d2.getTime();

            if (ld1 > ld2)
                smaller.add(j);
            else
                greater.add(j);
        }
        smaller=sortByPoints(smaller);
        greater=sortByPoints(greater);
        smaller.add(pivot);
        smaller.addAll(greater);
        sorted = smaller;

        return sorted;
    }
}
