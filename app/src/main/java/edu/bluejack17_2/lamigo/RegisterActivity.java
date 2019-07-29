package edu.bluejack17_2.lamigo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button createAccountBtn,toLoginBtn;
    EditText emailTxt,passwordTxt,usernameTxt;
    String email,username,password,errorMessage;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        setListener();
    }

    public void toLoginActivity(){
        Intent toLogin = new Intent(this,MainActivity.class);
        startActivity(toLogin);
        //finish();
    }

    public boolean isValidEmail(String email){
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(email.matches(pattern)){
            return true;
        }else{
            return false;
        }
    }

    public boolean isValidUsername(String username){
        String stringPattern = ".*[A-Za-z].*";
        String digitPattern = ".*[0-9].*";
        String alphanumericPattern = "[A-Za-z0-9]*";

        if(username.matches(stringPattern) && username.matches(digitPattern) && username.matches(alphanumericPattern)){
            return true;
        }else{
            return false;
        }
    }

    public boolean isValidPassword(String password){
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]$";
        if(password.matches(passwordPattern)){
            return true;
        }else{
            return false;
        }

    }

    public void fillErrorMessage(int err){
        if (err == 1){
            errorMessage = getResources().getString(R.string.email_invalid);
        } else if(err == 2){
            errorMessage = getResources().getString(R.string.username_invalid);
        } else if (err == 3){
            errorMessage = getResources().getString(R.string.username_taken);
        } else if (err == 4){
            errorMessage = getResources().getString(R.string.password_length);
        } else if (err == 5){
            errorMessage = getResources().getString(R.string.password_characters) ;
        }
    }

    public boolean validate() {
        int err;
        email = emailTxt.getText().toString();
        password = passwordTxt.getText().toString();
        username = usernameTxt.getText().toString();

        if (!isValidEmail(email)) {
            err = 1;
            fillErrorMessage(err);
            return false;
        }
        if (!isValidUsername(username))
        {
            err = 2;
            fillErrorMessage(err);
            return false;
        }
        if (password.length() <= 5 || password.length() > 50)
        {
            err = 4;
            fillErrorMessage(err);
            return false;
        }
//        if (!isValidPassword(password))
//        {
//            err = 5;
//            fillErrorMessage(err);
//            return false;
//        }
        return true;
    }

    public void showError(){
        Toast t = Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM,0,0);
        t.show();
    }

    public void createAccount(final String email, final String username, final String password){
        // insert register account code here
        mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    // kalo berhasil register
                    String userid = mAuth.getCurrentUser().getUid();
                    Toast.makeText(RegisterActivity.this , "ID : " + userid , Toast.LENGTH_LONG).show();

                    User user = new User(username , email , password);
                    mDatabase.child("User").child(userid).setValue(user);

                }else{
                    // kalo gagal register

                }
            }
        });
    }

    private void writeNewUser(String userID , String name , String email , String password){


    }

    public void setListener(){
        toLoginBtn = findViewById(R.id.backToLoginBtn);
        emailTxt = findViewById(R.id.emailTxt);
        usernameTxt = findViewById(R.id.usernameRegisTxt);
        passwordTxt = findViewById(R.id.passwordRegisTxt);
        createAccountBtn = findViewById(R.id.createAccBtn);

        toLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toLoginActivity();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (validate())
                {
                    createAccount(email,username,password);
                    toLoginActivity();
                }else{
                    showError();
                }
            }
        });
    }


}
