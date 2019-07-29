package edu.bluejack17_2.lamigo;

import java.util.ArrayList;

public class ThreadShared {

    private static RecyclerViewAdapter rvAdapter;

    private ThreadShared(){

    }

    public static RecyclerViewAdapter getInstance(ArrayList<Thread> th, ContentFragment cf){

        if(rvAdapter == null){
            rvAdapter = new RecyclerViewAdapter(th, cf);
        }

        return rvAdapter;
    }

    public static RecyclerViewAdapter getInstance() {
        return (RecyclerViewAdapter) rvAdapter;
    }

}
