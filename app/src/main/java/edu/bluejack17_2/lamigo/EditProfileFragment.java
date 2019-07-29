package edu.bluejack17_2.lamigo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileFragment extends Fragment {

    Button cancelBtn,saveBtn;
    EditText editEmail,editPhone,editAddress,editDob;
    String currEmail,currPhone,currAddress,currDob;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile,container,false);
        cancelBtn = view.findViewById(R.id.cancelEditBtn);
        saveBtn = view.findViewById(R.id.saveEdit);

        //editEmail = view.findViewById(R.id.editEmail);
        editPhone = view.findViewById(R.id.editPhone);
        editAddress = view.findViewById(R.id.editAddress);
        editDob = view.findViewById(R.id.editBirth);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ForumActivity)getActivity()).setView(0);
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String userid = currentUser.getUid();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save profile here
                //currEmail = editEmail.getText().toString();
                currPhone = editPhone.getText().toString();
                currAddress = editAddress.getText().toString();
                currDob = editDob.getText().toString();

                //mDatabase.child("User").child(userid).child("email").setValue(currEmail);
                mDatabase.child("User").child(userid).child("address").setValue(currAddress);
                mDatabase.child("User").child(userid).child("phone").setValue(currPhone);
                mDatabase.child("User").child(userid).child("dob").setValue(currDob);

                //back to show profile
                ((ForumActivity)getActivity()).setView(1);
            }
        });

        return view;
    }

}
