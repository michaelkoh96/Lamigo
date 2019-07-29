package edu.bluejack17_2.lamigo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    Button toEditBtn;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    TextView email , name , phoneview , dobview , addressview ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_layout,container,false);
        toEditBtn = view.findViewById(R.id.editBtn);
        name = view.findViewById(R.id.name_profile);
        email = view.findViewById(R.id.textView9);
        phoneview = view.findViewById(R.id.textView11);
        dobview = view.findViewById(R.id.textView12);
        addressview = view.findViewById(R.id.textView10);

        toEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ForumActivity)getActivity()).setView(3);

            }
        });

         FirebaseUser currentUser = mAuth.getCurrentUser();
         final String userid = currentUser.getUid();

        ValueEventListener profilelisten = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nametxt = dataSnapshot.child("User").child(userid).child("username").getValue().toString();
                String emailtxt = dataSnapshot.child("User").child(userid).child("email").getValue().toString();

                String phonetxt = dataSnapshot.child("User").child(userid).child("phone").getValue().toString();
                String dobtxt = dataSnapshot.child("User").child(userid).child("dob").getValue().toString();
                String addresstxt = dataSnapshot.child("User").child(userid).child("address").getValue().toString();

                name.setText(nametxt);
                email.setText(emailtxt);
                phoneview.setText(phonetxt);
                dobview.setText(dobtxt);
                addressview.setText(addresstxt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

         mDatabase.addValueEventListener(profilelisten);
        // set profile view
//        if(mAuth.getCurrentUser() != null)
//        {
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//
//            String userid = currentUser.getUid();
//            String name =  mDatabase.child("User").child(userid).child("username").
//
//        }else{
//
//        }


        return view;
    }

}
