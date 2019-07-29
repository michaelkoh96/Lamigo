package edu.bluejack17_2.lamigo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ForumActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , ChildEventListener {

    private MyFragmentManager fm;
    private ViewPager vp;
    private  Toolbar toolbar;
    private MenuItem search,filter,filter2,filter3,filter4,filter5;
    private FloatingActionButton fab;
    private ActionBarDrawerToggle toggle;
    private TextView email,name;
    private RecyclerViewAdapter rvAdapter;
    private ArrayList<Thread> tempThreadList = new ArrayList<>();
    private ArrayList<Thread> filterLanguage = new ArrayList<>();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();




    void showNotification(String title, String content) {

        if(mAuth.getCurrentUser()==null)
        {
            return;
        }

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.logolamigo) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                //.setSound(alarmSound) // set alarm sound for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), ForumActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(2);
                toolbar.setTitle(fm.getTitle(vp.getCurrentItem()));
                search.setVisible(false);
                filter.setVisible(false);
                filter2.setVisible(false);
                filter3.setVisible(false);
                filter4.setVisible(false);
                filter5.setVisible(false);
                fab.setVisibility(View.GONE);
                toggle.setDrawerIndicatorEnabled(true);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigation view listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showNotification("Hello!" , "You are logged in");

        //set viewpager and fragment adapter
        vp = findViewById(R.id.container);
        //disable swiping to change fragment
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        fm = new MyFragmentManager(getSupportFragmentManager());
        fm.addFragment(new ContentFragment(),"Forum");
        fm.addFragment(new ProfileFragment(),"Profile");
        fm.addFragment(new NewPostFragment(),"New Post");
        fm.addFragment(new EditProfileFragment(),"Edit Profile");
        vp.setAdapter(fm);
        vp.setCurrentItem(0);

        //set nama , email di nav drawer
        name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name_drawer);
        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_drawer);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String userid = currentUser.getUid();




        mDatabase.child("User").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);

                name.setText(us.username);
                email.setText(us.email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setView(int pos){
        vp.setCurrentItem(pos);
        if(pos==0){
            search.setVisible(true);
            fab.setVisibility(View.VISIBLE);
            filter.setVisible(true);
            filter2.setVisible(true);
            filter3.setVisible(true);
            filter4.setVisible(true);
            filter5.setVisible(true);
            toggle.setDrawerIndicatorEnabled(true);
        }else{
            search.setVisible(false);
            fab.setVisibility(View.GONE);
            filter.setVisible(false);
            filter2.setVisible(false);
            filter3.setVisible(false);
            filter4.setVisible(false);
            filter5.setVisible(false);

            if (pos==3){
                toggle.setDrawerIndicatorEnabled(false);
            }
        }

        toolbar.setTitle(fm.getTitle(vp.getCurrentItem()));
    }



    public ContentFragment getContentFragment()
    {
        return (ContentFragment) fm.getItem(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(vp.getCurrentItem() != 0)
            {
                vp.setCurrentItem(0);
                toolbar.setTitle(fm.getTitle(vp.getCurrentItem()));
                search.setVisible(true);
                filter.setVisible(true);
                filter2.setVisible(true);
                filter3.setVisible(true);
                filter4.setVisible(true);
                filter5.setVisible(true);
                fab.setVisibility(View.VISIBLE);
                toggle.setDrawerIndicatorEnabled(true);
                NewPostFragment.title.setText("");
                NewPostFragment.postDesc.setText("");
                NewPostFragment.languageSpinner.setSelection(0);

            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forum, menu);
        search = menu.findItem(R.id.action_search);
        filter = menu.findItem(R.id.filter);
        filter2 = menu.findItem(R.id.filter2);
        filter3 = menu.findItem(R.id.filter3);
        filter4 = menu.findItem(R.id.filter4);
        filter5 = menu.findItem(R.id.filter5);

        android.widget.SearchView searchView = (android.widget.SearchView) search.getActionView();
        rvAdapter = ThreadShared.getInstance();

        fillTempThreadList();

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // live search saat ketik
                //rvAdapter.getFilter().filter(newText);
//                Toast.makeText(ForumActivity.this, newText, Toast.LENGTH_SHORT).show();
                //Toast.makeText(ForumActivity.this, Integer.toString(tempThreadList.size()), Toast.LENGTH_SHORT).show();
                rvAdapter.filteredThreads.clear();

                if (newText.isEmpty()) {
                    getContentFragment().refresh();
                } else {
                    //Toast.makeText(ForumActivity.this, tempThreadList.size(), Toast.LENGTH_SHORT).show();
                    for (Thread thread : tempThreadList) {

                            if (thread.title.toLowerCase().contains(newText.toLowerCase())) {
                                rvAdapter.filteredThreads.add(thread);
                            }
                    }
                    rvAdapter.setData(rvAdapter.filteredThreads);
                    rvAdapter.notifyDataSetChanged();
                }




                return false;
            }
        });
        return true;
    }

    public void fillTempThreadList(){
        try{
            tempThreadList = rvAdapter.threads;
        }catch (Exception e){
            tempThreadList = new ArrayList<>();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }else if(id == R.id.filter){
            int i =0;
            rvAdapter.filteredThreads.clear();
            for(Thread th : tempThreadList){
                if(th.language.equals("English")){
                    rvAdapter.filteredThreads.add(th);
                }
                i++;
            }
            rvAdapter.setData(rvAdapter.filteredThreads);
            rvAdapter.notifyDataSetChanged();
        }else if(id == R.id.filter2){
            int i =0;
            rvAdapter.filteredThreads.clear();
            for(Thread th : tempThreadList){
                if(th.language.equals("Indonesian")){
                    rvAdapter.filteredThreads.add(th);
                }
                i++;
            }
            rvAdapter.setData(rvAdapter.filteredThreads);
            rvAdapter.notifyDataSetChanged();
        }else if(id == R.id.filter3){
            int i =0;
            rvAdapter.filteredThreads.clear();
            for(Thread th : tempThreadList){
                if(th.language.equals("France")){
                    rvAdapter.filteredThreads.add(th);
                }
                i++;
            }
            rvAdapter.setData(rvAdapter.filteredThreads);
            rvAdapter.notifyDataSetChanged();
        }else if(id == R.id.filter4){
            int i =0;
            rvAdapter.filteredThreads.clear();
            for(Thread th : tempThreadList){
                if(th.language.equals("Spanish")){
                    rvAdapter.filteredThreads.add(th);
                }
                i++;
            }
            rvAdapter.setData(rvAdapter.filteredThreads);
            rvAdapter.notifyDataSetChanged();
        }else if(id == R.id.filter5){
            int i =0;
            rvAdapter.filteredThreads.clear();
            for(Thread th : tempThreadList){
                if(th.language.equals("German")){
                    rvAdapter.filteredThreads.add(th);
                }
                i++;
            }
            rvAdapter.setData(rvAdapter.filteredThreads);
            rvAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            //coding profile
            vp.setCurrentItem(1);
            search.setVisible(false);
            fab.setVisibility(View.GONE);
            filter.setVisible(false);
            filter2.setVisible(false);
            filter3.setVisible(false);
            filter4.setVisible(false);
            filter5.setVisible(false);
            toolbar.setTitle(fm.getTitle(vp.getCurrentItem()));
            toggle.setDrawerIndicatorEnabled(true);
            NewPostFragment.title.setText("");
            NewPostFragment.postDesc.setText("");
            NewPostFragment.languageSpinner.setSelection(0);
        } else if (id == R.id.nav_logout) {
            //coding logout disini
            FirebaseAuth.getInstance().signOut();

            //cancel notif
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            Intent toLogin = new Intent(this,MainActivity.class);
            startActivity(toLogin);
            finish();
        }else if(id == R.id.nav_forum){
            vp.setCurrentItem(0);
            toolbar.setTitle(fm.getTitle(vp.getCurrentItem()));
            search.setVisible(true);
            filter.setVisible(true);
            filter2.setVisible(true);
            filter3.setVisible(true);
            filter4.setVisible(true);
            filter5.setVisible(true);
            fab.setVisibility(View.VISIBLE);
            NewPostFragment.title.setText("");
            NewPostFragment.postDesc.setText("");
            NewPostFragment.languageSpinner.setSelection(0);
            toggle.setDrawerIndicatorEnabled(true);
        }else if(id == R.id.nav_new){
            vp.setCurrentItem(2);
            toolbar.setTitle(fm.getTitle(vp.getCurrentItem()));
            search.setVisible(false);
            filter.setVisible(false);
            filter2.setVisible(false);
            filter3.setVisible(false);
            filter4.setVisible(false);
            filter5.setVisible(false);
            fab.setVisibility(View.GONE);
            NewPostFragment.title.setText("");
            NewPostFragment.postDesc.setText("");
            NewPostFragment.languageSpinner.setSelection(0);
            toggle.setDrawerIndicatorEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
