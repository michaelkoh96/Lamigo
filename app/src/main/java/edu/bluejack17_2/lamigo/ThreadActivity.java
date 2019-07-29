package edu.bluejack17_2.lamigo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ThreadActivity extends AppCompatActivity implements ChildEventListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewPager mViewPager;
    public static Bundle extras;
    public static int sortMode = 0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    void showNotification(String title, String content) {
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
                .setSound(RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND)) // set alarm sound for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), ForumActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new ThreadFragment());
        mSectionsPagerAdapter.addFragment(new ReplyFragment());

        mDatabase.child("Comment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    CommentAdapter.replyDesc = "";
                    CommentAdapter.replyTo = "";
                }else if(position==1){
                    ReplyFragment.empty();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        extras = getIntent().getExtras();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thread, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sortNewest) {
            sortMode = 0;
            ThreadFragment.sortComments(sortMode);
        }else if(id == R.id.sortBest){
            sortMode=1;
            ThreadFragment.sortComments(sortMode);
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ThreadFragment extends Fragment{

        public static SwipeRefreshLayout srl2;
        public static RecyclerView rv2;
        public static CommentAdapter rvAdapter2;
        public static ArrayList<Comment> comments;

        static FirebaseAuth mAuth = FirebaseAuth.getInstance();

        static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_thread,container,false);
            srl2 = view.findViewById(R.id.srl2);
            rv2 = view.findViewById(R.id.recyclerView2);
            rv2.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv2.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            rvAdapter2 = new CommentAdapter(comments,this,ThreadActivity.extras);
            rv2.setAdapter(rvAdapter2);
            return view;
        }

        @Override
        public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);



            srl2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // code buat saat direfresh (fill ulang)
                    refresh();
                }
            });
            refresh();
            sortComments(sortMode);

//            mDatabase.child("Comment").addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    Comment com = dataSnapshot.getValue(Comment.class);
//                    Toast.makeText(view.getContext(), com.userid, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

        }

        public static void sortComments(int sortMode){
            if(sortMode == 1){
                comments = Comment.sortByPoints(comments);
            }else if(sortMode == 0){
                comments = Comment.sortByDateNew(comments);
            }

            rvAdapter2.setData(comments);
            rvAdapter2.notifyDataSetChanged();
        }

        public static void refresh()
        {
            fill();
            rvAdapter2.setData(comments);
            rv2.setAdapter(rvAdapter2);
        }
        static void fill() {
            comments = new ArrayList<>();
            srl2.setRefreshing(true);

            // animasi refresh
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    srl2.setRefreshing(false);
                }
            },1000);

            //fill comment list here
//            comments.add(new Comment("1","dasd","10", "20/08/2018 22:00:00", "Description", "us123", "ferdy"));
//            comments.add(new Comment("1","dasd","40", "22/08/2018 21:00:00", "Description4", "us123", "ferdy"));
//            comments.add(new Comment("1","dasd","30", "19/08/2018 23:00:00", "Description3", "us123", "ferdy"));
//            comments.add(new Comment("1","dasd","20", "21/08/2018 24:00:00", "Description2", "us123", "ferdy"));
//            comments.add(new Comment("1","dasd","50", "22/08/2018 20:00:00", "Description2", "us123", "ferdy"));


            mDatabase.child("Comment").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Comment thr = snapshot.getValue(Comment.class);
                        if(extras.getString("id").equals(thr.threadId)) {
                            comments.add(new Comment(thr.id, thr.threadId, thr.points, thr.date, thr.desc, thr.userid, thr.username));
                        }

                    }
                    comments.add(new Comment("1","dasd","-999", "1/1/1990", "Description", "us123", "ferdy"));
                    rvAdapter2.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public static class ReplyFragment extends Fragment{

        public Button replyBtn;
        public static TextView replyDesc;
        public String threadId,username;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.reply_fragment,container,false);

            replyBtn = view.findViewById(R.id.submitReply);
            replyDesc = view.findViewById(R.id.replyDesc);

            threadId = extras.getString("id");
            final String userid = mAuth.getCurrentUser().getUid();

            mDatabase.child("User").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User us = dataSnapshot.getValue(User.class);

                    username = us.username;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!CommentAdapter.replyDesc.equals(""))
                    {
                        String quote,reply;
                        if(CommentAdapter.replyDesc.contains("\n\n"))
                        {
                            quote = "\""+ CommentAdapter.replyDesc.substring(CommentAdapter.replyDesc.indexOf("\n\n")+2,CommentAdapter.replyDesc.length())+"\" - by "+CommentAdapter.replyTo+"\n\n";

                        }else{
                            quote = "\""+ CommentAdapter.replyDesc +"\" - by "+CommentAdapter.replyTo+"\n\n";
                        }

                        reply = replyDesc.getText().toString();

                        replyDesc.setText(quote+reply);

                        if(replyDesc.getText().toString().equals(""))
                        {
                            Toast.makeText(view.getContext(), "Comment must be filled!", Toast.LENGTH_SHORT).show();
                        }else{
                            // add comment sesuai thread id nya

                            String commentid = Comment.generateID();

                            Comment  cmt = new Comment(commentid, threadId,"0" , getDate(),replyDesc.getText().toString(),userid,username);
                            //Comment  cmt = new Comment(commentid, threadId,Integer.toString(new Random().nextInt(100)) , getDate(),replyDesc.getText().toString(),userid,username);
                            mDatabase.child("Comment").child(commentid).setValue(cmt);

                            ThreadActivity.mViewPager.setCurrentItem(0);
                            ThreadFragment.refresh();
                        }

                    }else{
                        if(replyDesc.getText().toString().equals(""))
                        {
                            Toast.makeText(view.getContext(), "Comment must be filled!", Toast.LENGTH_SHORT).show();
                        }else{
                            // add comment sesuai thread id nya

                            String commentid = Comment.generateID();

                            Comment  cmt = new Comment(commentid, threadId,"0" , getDate(),replyDesc.getText().toString(),userid,username);
                            //Comment  cmt = new Comment(commentid, threadId,Integer.toString(new Random().nextInt(100)) , getDate(),replyDesc.getText().toString(),userid,username);
                            mDatabase.child("Comment").child(commentid).setValue(cmt);

                            ThreadActivity.mViewPager.setCurrentItem(0);
                            ThreadFragment.refresh();
                        }
                    }
                }
            });

            return view;
        }

        public String getDate()
        {
            //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            return formatter.format(date).toString();
        }

        public static void empty(){
            replyDesc.setText("");
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> tabFragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment f){
            tabFragmentList.add(f);
        }

        @Override
        public Fragment getItem(int position) {

            return tabFragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
