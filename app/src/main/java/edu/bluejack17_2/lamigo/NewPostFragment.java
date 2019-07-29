package edu.bluejack17_2.lamigo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPostFragment extends Fragment {

    public static TextView postDesc,title;
    private Button submit;
    private String descTxt,titleTxt;
    private String date;
    private String id;
    private String author,language;
    public static Spinner languageSpinner;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_post,container,false);

        postDesc = view.findViewById(R.id.newPostDescription);
        title = view.findViewById(R.id.newPostTitle);
        submit = view.findViewById(R.id.submitPostBtn);
        languageSpinner = view.findViewById(R.id.language);
        final String userid = mAuth.getCurrentUser().getUid();

        mDatabase.child("User").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User us = dataSnapshot.getValue(User.class);

                author = us.username;
                System.out.print(us.username + "name");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                titleTxt = title.getText().toString();
                descTxt = postDesc.getText().toString();
                id = Thread.generateID();
                date = getDate();
                language = languageSpinner.getSelectedItem().toString();



                //get nama user skrg
                //System.out.print(author);


                if(titleTxt.equals("") || descTxt.equals("")){
                    Toast.makeText(view.getContext(), "Title and Description must be filled", Toast.LENGTH_SHORT).show();
                }else if(languageSpinner.getSelectedItemPosition()==0){
                    Toast.makeText(view.getContext(), "Choose the language", Toast.LENGTH_SHORT).show();
                }else {
                    Thread  thr = new Thread(id , titleTxt , "0", date , descTxt , userid , author , language);
                    mDatabase.child("Thread").child(id).setValue(thr);
                    ((ForumActivity)getActivity()).setView(0);

                    NewPostFragment.title.setText("");
                    NewPostFragment.postDesc.setText("");
                    NewPostFragment.languageSpinner.setSelection(0);
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
}
