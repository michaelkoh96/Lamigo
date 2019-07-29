package edu.bluejack17_2.lamigo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContentFragment extends Fragment {

    public RecyclerView rv;
    public RecyclerViewAdapter rvAdapter;
    public SwipeRefreshLayout srl;
    public  ArrayList<Thread> threads;
    private MenuItem search;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_fragment,container,false);
        srl = view.findViewById(R.id.srl);

        rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //rvAdapter = new RecyclerViewAdapter(threads);
        rvAdapter = ThreadShared.getInstance(threads,this);
        rv.setAdapter(rvAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // code buat saat direfresh (fill ulang)
                refresh();
            }
        });
        refresh();
        ((ForumActivity)getActivity()).fillTempThreadList();

    }

    public void refresh()
    {
        fill();
        rvAdapter.setData(threads);
        rv.setAdapter(rvAdapter);
    }

    void fill(){
        threads = new ArrayList<>();
        srl.setRefreshing(true);

        // animasi refresh
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(false);
            }
        },1000);

        //fill forum list here

        mDatabase.child("Thread").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   Thread thr = snapshot.getValue(Thread.class);

                   threads.add(new Thread(thr.id , thr.title ,thr.score , thr.date , thr.desc , thr.userid , thr.author , thr.language));
                   rvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
